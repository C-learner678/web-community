package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.constant.RocketmqConstant;
import com.jlu.webcommunity.core.PageParam;
import com.jlu.webcommunity.core.RedisClient;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.core.rocketmq.RocketmqBody;
import com.jlu.webcommunity.core.rocketmq.RocketmqProducer;
import com.jlu.webcommunity.dao.CommentDao;
import com.jlu.webcommunity.dao.PostDao;
import com.jlu.webcommunity.dao.UserFootDao;
import com.jlu.webcommunity.entity.Comment;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.UserFoot;
import com.jlu.webcommunity.entity.dto.userFoot.*;
import com.jlu.webcommunity.entity.vo.post.GetPostByPageVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetCommentUserFootCountVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootCountVo;
import com.jlu.webcommunity.entity.vo.userFoot.GetPostUserFootVo;
import com.jlu.webcommunity.enums.UserFootTypeEnum;
import com.jlu.webcommunity.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class UserFootServiceImpl implements UserFootService {
    @Autowired
    private UserFootDao userFootDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RocketmqProducer rocketmqProducer;

    // 帖子足迹
    @Override
    public GetPostUserFootVo getPostUserFoot(GetPostUserFootDto dto) {
        QueryWrapper<UserFoot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.eq("user_id", UserContext.getUserData().getId());
        queryWrapper.eq("post_id", dto.getPostId());
        queryWrapper.eq("is_post", true);
        UserFoot userFoot = userFootDao.getOne(queryWrapper);
        GetPostUserFootVo vo = new GetPostUserFootVo();
        vo.setReadStatus(false);
        vo.setLikeStatus(false);
        vo.setCollectStatus(false);
        if (userFoot != null) {
            vo.setReadStatus(userFoot.getReadStatus());
            vo.setLikeStatus(userFoot.getLikeStatus());
            vo.setCollectStatus(userFoot.getCollectStatus());
        }
        return vo;
    }

    @Override
    public Boolean modifyPostUserFoot(ModifyPostUserFootDto dto) {
        Post post = postDao.getById(dto.getPostId());
        if(post == null || post.getDeleted()){
            return false;
        }
        // 因为不是所有情况下都要确认是不是帖子还存在，所以分出以下这个函数
        modifyPostUserFoot1(dto);
        return true;
    }

    @Override
    public void modifyPostUserFoot1(ModifyPostUserFootDto dto){
        QueryWrapper<UserFoot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.eq("user_id", UserContext.getUserData().getId());
        queryWrapper.eq("post_id", dto.getPostId());
        queryWrapper.eq("is_post", true);
        UserFoot userFoot = userFootDao.getOne(queryWrapper);
        if(userFoot == null){
            userFoot = new UserFoot();
            userFoot.setUserId(UserContext.getUserData().getId());
            userFoot.setPostId(dto.getPostId());
            userFoot.setIsPost(true);
            userFoot.setReadStatus(false);
            userFoot.setLikeStatus(false);
            userFoot.setCollectStatus(false);
            userFoot.setDeleted(false);
            userFoot.setCreateTime(new Date());
            userFoot.setUpdateTime(new Date());
            if(dto.getType().equals(UserFootTypeEnum.READ)){
                userFoot.setReadStatus(dto.getPositive());
            }else if(dto.getType().equals(UserFootTypeEnum.LIKE)){
                userFoot.setLikeStatus(dto.getPositive());
            }else if(dto.getType().equals(UserFootTypeEnum.COLLECT)){
                userFoot.setCollectStatus(dto.getPositive());
            }
            userFootDao.save(userFoot);
        }else{
            if(dto.getType().equals(UserFootTypeEnum.READ)){
                if(userFoot.getReadStatus().equals(dto.getPositive())){
                    return;
                }
                userFoot.setReadStatus(dto.getPositive());
                userFoot.setUpdateTime(new Date());
            }else if(dto.getType().equals(UserFootTypeEnum.LIKE)){
                if(userFoot.getLikeStatus().equals(dto.getPositive())){
                    return;
                }
                userFoot.setLikeStatus(dto.getPositive());
                userFoot.setUpdateTime(new Date());
            }else if(dto.getType().equals(UserFootTypeEnum.COLLECT)){
                if(userFoot.getCollectStatus().equals(dto.getPositive())){
                    return;
                }
                userFoot.setCollectStatus(dto.getPositive());
                userFoot.setUpdateTime(new Date());
            }
            userFootDao.updateById(userFoot);
        }
        // 统计
        if(dto.getPositive()) {
            if(dto.getType().equals(UserFootTypeEnum.READ)){
                redisClient.hIncr(RedisConstant.postStatisticKey + dto.getPostId(),
                        RedisConstant.postReadNumKey, 1L);
            }else if(dto.getType().equals(UserFootTypeEnum.LIKE)){
                redisClient.hIncr(RedisConstant.postStatisticKey + dto.getPostId(),
                        RedisConstant.postLikeNumKey, 1L);
            }else if(dto.getType().equals(UserFootTypeEnum.COLLECT)){
                redisClient.hIncr(RedisConstant.postStatisticKey + dto.getPostId(),
                        RedisConstant.postCollectNumKey, 1L);
            }
        }else{
            if(dto.getType().equals(UserFootTypeEnum.READ)){
                redisClient.hIncr(RedisConstant.postStatisticKey + dto.getPostId(),
                        RedisConstant.postReadNumKey, -1L);
            }else if(dto.getType().equals(UserFootTypeEnum.LIKE)){
                redisClient.hIncr(RedisConstant.postStatisticKey + dto.getPostId(),
                        RedisConstant.postLikeNumKey, -1L);
            }else if(dto.getType().equals(UserFootTypeEnum.COLLECT)){
                redisClient.hIncr(RedisConstant.postStatisticKey + dto.getPostId(),
                        RedisConstant.postCollectNumKey, -1L);
            }
        }
        // 通知
        if(dto.getType().equals(UserFootTypeEnum.LIKE) && dto.getPositive()){
            RocketmqBody body = new RocketmqBody();
            body.setFromUserId(UserContext.getUserData().getId());
            body.setPostId(dto.getPostId());
            body.setType(MessageTypeConstant.ADD_LIKE_POST);
            rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        }
    }

    // 获取收藏帖子
    @Override
    public List<GetPostByPageVo> getCollectPostByPage(GetCollectPostByPageDto dto) {
        List<GetPostByPageVo> posts = userFootDao.getBaseMapper().selectCollectPostByPage(
                UserContext.getUserData().getId(),
                PageParam.getInstance(dto.getPageNum(), dto.getPageSize()));
        for(GetPostByPageVo post: posts){
            post.setReadCnt((Integer) redisClient.hGet(RedisConstant.postStatisticKey + post.getId(),
                    RedisConstant.postReadNumKey));
            post.setLikeCnt((Integer) redisClient.hGet(RedisConstant.postStatisticKey + post.getId(),
                    RedisConstant.postLikeNumKey));
        }
        return posts;
    }

    // 帖子足迹统计
    @Override
    public GetPostUserFootCountVo getPostUserFootCount(GetPostUserFootCountDto dto) {
        GetPostUserFootCountVo vo = new GetPostUserFootCountVo();
        vo.setReadCnt((Integer) redisClient.hGet(RedisConstant.postStatisticKey + dto.getPostId(),
                RedisConstant.postReadNumKey));
        vo.setLikeCnt((Integer) redisClient.hGet(RedisConstant.postStatisticKey + dto.getPostId(),
                RedisConstant.postLikeNumKey));
        vo.setCollectCnt((Integer) redisClient.hGet(RedisConstant.postStatisticKey + dto.getPostId(),
                RedisConstant.postCollectNumKey));
        return vo;
    }

    /*
    @Override
    public GetCommentUserFootVo getCommentUserFoot(GetCommentUserFootDto dto) {
        QueryWrapper<UserFoot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.eq("user_id", dto.getUserId());
        queryWrapper.eq("post_id", dto.getCommentId());
        queryWrapper.eq("is_post", false);
        UserFoot userFoot = userFootDao.getOne(queryWrapper);
        GetCommentUserFootVo vo = new GetCommentUserFootVo();
        vo.setLikeStatus(false);
        if (userFoot != null) {
            vo.setLikeStatus(userFoot.getLikeStatus());
        }
        return vo;
    }
    */

    // 评论足迹（只有喜欢，没有已读和收藏）
    @Override
    public Boolean modifyCommentUserFoot(ModifyCommentUserFootDto dto) {
        Comment comment = commentDao.getById(dto.getCommentId());
        if(comment == null || comment.getDeleted()){
            return false;
        }
        QueryWrapper<UserFoot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.eq("user_id", UserContext.getUserData().getId());
        queryWrapper.eq("post_id", dto.getCommentId());
        queryWrapper.eq("is_post", false);
        UserFoot userFoot = userFootDao.getOne(queryWrapper);
        if(userFoot == null){
            userFoot = new UserFoot();
            userFoot.setUserId(UserContext.getUserData().getId());
            userFoot.setPostId(dto.getCommentId());
            userFoot.setIsPost(false);
            userFoot.setLikeStatus(dto.getPositive());
            userFoot.setDeleted(false);
            userFoot.setCreateTime(new Date());
            userFoot.setUpdateTime(new Date());
            userFootDao.save(userFoot);
        }else{
            if(userFoot.getLikeStatus().equals(dto.getPositive())){
                return true;
            }
            userFoot.setLikeStatus(dto.getPositive());
            userFoot.setUpdateTime(new Date());
            userFootDao.updateById(userFoot);
        }
        // 统计
        if(dto.getPositive()) {
            redisClient.incr(RedisConstant.commentLikeNumKey + dto.getCommentId(), 1L);
        }else{
            redisClient.incr(RedisConstant.commentLikeNumKey + dto.getCommentId(), -1L);
        }
        // 通知
        if(dto.getPositive()){
            RocketmqBody body = new RocketmqBody();
            body.setFromUserId(UserContext.getUserData().getId());
            body.setCommentId(dto.getCommentId());
            body.setType(MessageTypeConstant.ADD_LIKE_COMMENT);
            rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        }
        return true;
    }

    // 统计每个帖子的已读、喜欢、收藏情况
    @Override
    public void countUserFoot(){
        List<GetPostUserFootCountVo> list = userFootDao.getBaseMapper().selectPostFootCount();
        for(GetPostUserFootCountVo vo: list){
            redisClient.hSet(RedisConstant.postStatisticKey + vo.getPostId(),
                    RedisConstant.postReadNumKey, vo.getReadCnt());
            redisClient.hSet(RedisConstant.postStatisticKey + vo.getPostId(),
                    RedisConstant.postLikeNumKey, vo.getLikeCnt());
            redisClient.hSet(RedisConstant.postStatisticKey + vo.getPostId(),
                    RedisConstant.postCollectNumKey, vo.getCollectCnt());
        }
        List<GetCommentUserFootCountVo> list1 = userFootDao.getBaseMapper().selectCommentFootCount();
        for(GetCommentUserFootCountVo vo: list1){
            redisClient.set(RedisConstant.commentLikeNumKey + vo.getCommentId(), vo.getLikeCnt());
        }
    }

    @PostConstruct
    public void init(){
        this.countUserFoot();
    }
}
