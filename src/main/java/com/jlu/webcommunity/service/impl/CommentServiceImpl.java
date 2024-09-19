package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.constant.RocketmqConstant;
import com.jlu.webcommunity.core.RedisClient;
import com.jlu.webcommunity.core.rocketmq.RocketmqBody;
import com.jlu.webcommunity.core.rocketmq.RocketmqProducer;
import com.jlu.webcommunity.dao.CommentDao;
import com.jlu.webcommunity.dao.PostDao;
import com.jlu.webcommunity.dao.UserFootDao;
import com.jlu.webcommunity.entity.Comment;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.UserFoot;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.comment.AddCommentDto;
import com.jlu.webcommunity.entity.dto.comment.GetCommentByPageDto;
import com.jlu.webcommunity.entity.dto.comment.GetCommentCountDto;
import com.jlu.webcommunity.entity.vo.comment.GetCommentByPageVo;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.service.CommentService;
import com.jlu.webcommunity.core.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserFootDao userFootDao;

    @Autowired
    private RocketmqProducer rocketmqProducer;

    @Autowired
    private RedisClient redisClient;

    // 获取评论，把点赞数和当前用户是否点赞也返回
    @Override
    public List<GetCommentByPageVo> getCommentByPage(GetCommentByPageDto dto) {
        Long userId = UserContext.getUserData().getId();
        List<GetCommentByPageVo> list = commentDao.getBaseMapper().selectCommentByPage(dto.getPostId(), dto.getUserId(),
                PageParam.getInstance(dto.getPageNum(), dto.getPageSize()));
        if(userId == null){
            for(GetCommentByPageVo vo: list){
                if(redisClient.get(RedisConstant.commentLikeNumKey + vo.getId()) != null){
                    vo.setLikeCnt((Integer) redisClient.get(RedisConstant.commentLikeNumKey + vo.getId()));
                }else{
                    vo.setLikeCnt(0);
                }
                vo.setLikeStatus(false);
            }
        }else{
            for(GetCommentByPageVo vo: list){
                // 获取点赞数
                if(redisClient.get(RedisConstant.commentLikeNumKey + vo.getId()) != null){
                    vo.setLikeCnt((Integer) redisClient.get(RedisConstant.commentLikeNumKey + vo.getId()));
                }else{
                    vo.setLikeCnt(0);
                }
                // 获取当前用户点赞状态
                QueryWrapper<UserFoot> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id", userId);
                wrapper.eq("post_id", vo.getId());
                wrapper.eq("is_post", false);
                wrapper.eq("like_status", true);
                wrapper.eq("deleted", false);
                UserFoot userFoot = userFootDao.getOne(wrapper);
                vo.setLikeStatus(userFoot != null && userFoot.getLikeStatus());
            }
        }
        return list;
    }

    @Override
    public Integer getCommentCount(GetCommentCountDto dto) {
        return commentDao.getBaseMapper().selectCommentCount(dto.getPostId(), dto.getUserId());
    }

    @Override
    public boolean addComment(AddCommentDto dto) {
        Post post = postDao.getById(dto.getPostId());
        if(post.getDeleted()){
            return false;
        }
        Comment comment = new Comment();
        comment.setUserId(UserContext.getUserData().getId());
        comment.setPostId(dto.getPostId());
        comment.setContent(dto.getContent());
        comment.setDeleted(false);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        commentDao.save(comment);
        RocketmqBody body = new RocketmqBody();
        body.setFromUserId(UserContext.getUserData().getId());
        body.setRelateId(dto.getPostId());
        body.setType(MessageTypeConstant.ADD_COMMENT);
        body.setMessage(dto.getContent());
        rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        return true;
    }

    @Override
    public boolean deleteComment(Long id){
        Comment comment = commentDao.getById(id);
        if(comment != null) {
            comment.setDeleted(true);
            comment.setUpdateTime(new Date());
            commentDao.updateById(comment);
            RocketmqBody body = new RocketmqBody();
            body.setUserId(comment.getUserId());
            body.setRelateId(comment.getId());
            body.setType(MessageTypeConstant.DELETE_COMMENT);
            rocketmqProducer.syncSend(body, RocketmqConstant.topic);
            return true;
        }else{
            return false;
        }
    }
}
