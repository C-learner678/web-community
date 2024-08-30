package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.constant.RocketmqConstant;
import com.jlu.webcommunity.core.RedisClient;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.core.rocketmq.RocketmqBody;
import com.jlu.webcommunity.core.rocketmq.RocketmqProducer;
import com.jlu.webcommunity.dao.PostDao;
import com.jlu.webcommunity.dao.UserFootDao;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.UserFoot;
import com.jlu.webcommunity.entity.dto.userFoot.GetPostUserFootDto;
import com.jlu.webcommunity.entity.dto.userFoot.ModifyPostUserFootDto;
import com.jlu.webcommunity.entity.vo.GetPostUserFootVo;
import com.jlu.webcommunity.enums.UserFootTypeEnum;
import com.jlu.webcommunity.service.UserFootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserFootServiceImpl implements UserFootService {
    @Autowired
    private UserFootDao userFootDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RocketmqProducer rocketmqProducer;

    @Override
    public GetPostUserFootVo getPostUserFoot(GetPostUserFootDto dto) {
        QueryWrapper<UserFoot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted", false);
        queryWrapper.eq("user_id", dto.getUserId());
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
            }else if(dto.getType().equals(UserFootTypeEnum.LIKE)){
                if(userFoot.getLikeStatus().equals(dto.getPositive())){
                    return;
                }
                userFoot.setLikeStatus(dto.getPositive());
            }else if(dto.getType().equals(UserFootTypeEnum.COLLECT)){
                if(userFoot.getCollectStatus().equals(dto.getPositive())){
                    return;
                }
                userFoot.setCollectStatus(dto.getPositive());
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
            body.setRelateId(dto.getPostId());
            body.setType(MessageTypeConstant.ADD_LIKE_POST);
            rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        }
    }
}
