package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.constant.RocketmqConstant;
import com.jlu.webcommunity.core.rocketmq.RocketmqBody;
import com.jlu.webcommunity.core.rocketmq.RocketmqProducer;
import com.jlu.webcommunity.dao.UserFollowDao;
import com.jlu.webcommunity.dao.UserInfoDao;
import com.jlu.webcommunity.entity.UserFollow;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.userFollow.GetUserFollowByPageDto;
import com.jlu.webcommunity.entity.vo.GetUserFollowCountVo;
import com.jlu.webcommunity.entity.dto.userFollow.GetUserFollowerByPageDto;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.service.UserFollowService;
import com.jlu.webcommunity.core.PageParam;
import com.jlu.webcommunity.core.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowDao userFollowDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private RocketmqProducer rocketmqProducer;

    @Override
    public boolean isFollow(Long userId, Long followUserId) {
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("follow_user_id", followUserId);
        queryWrapper.eq("deleted", false);
        UserFollow userFollow = userFollowDao.getOne(queryWrapper);
        return userFollow != null;
    }

    @Override
    public boolean addFollow(Long userId) {
        Long followUserId = UserContext.getUserData().getId();
        if(followUserId.equals(userId)){ // 不能自己关注自己
            return false;
        }
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoDao.getOne(userInfoQueryWrapper);
        if(userInfo == null || userInfo.getDeleted()){ // 不能关注不存在的用户
            return false;
        }
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("follow_user_id", followUserId);
        UserFollow userFollow = userFollowDao.getOne(queryWrapper);
        if(userFollow != null && userFollow.getDeleted()){
            userFollow.setDeleted(false);
            userFollow.setUpdateTime(new Date());
        }else if(userFollow == null){
            userFollow = new UserFollow();
            userFollow.setUserId(userId);
            userFollow.setFollowUserId(followUserId);
            userFollow.setDeleted(false);
            userFollow.setCreateTime(new Date());
            userFollow.setUpdateTime(new Date());
        }else{
            return false;
        }
        userFollowDao.saveOrUpdate(userFollow);
        redisClient.hIncr(RedisConstant.userStatisticKey + followUserId,
                RedisConstant.userFollowNumKey, 1l);
        redisClient.hIncr(RedisConstant.userStatisticKey + userId,
                RedisConstant.userFollowerNumKey, 1l);
        // 发送消息
        RocketmqBody body = new RocketmqBody();
        body.setUserId(userId);
        body.setFromUserId(followUserId);
        body.setType(MessageTypeConstant.ADD_USER_FOLLOW);
        rocketmqProducer.syncSend(body, RocketmqConstant.topic);
        return true;
    }

    @Override
    public boolean removeFollow(Long userId) {
        Long followUserId = UserContext.getUserData().getId();
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("follow_user_id", followUserId);
        UserFollow userFollow = userFollowDao.getOne(queryWrapper);
        if(userFollow != null && !userFollow.getDeleted()){
            userFollow.setDeleted(true);
            userFollow.setUpdateTime(new Date());
            userFollowDao.saveOrUpdate(userFollow);
            redisClient.hIncr(RedisConstant.userStatisticKey + followUserId,
                    RedisConstant.userFollowNumKey, -1L);
            redisClient.hIncr(RedisConstant.userStatisticKey + userId,
                    RedisConstant.userFollowerNumKey, -1L);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<UserInfo> getFollow(GetUserFollowByPageDto dto) {
        return userFollowDao.getBaseMapper().selectUserFollowByPage(
                dto.getUserId(), PageParam.getInstance(dto.getPageNum(), dto.getPageSize()));
    }

    @Override
    public List<UserInfo> getFollower(GetUserFollowerByPageDto dto) {
        return userFollowDao.getBaseMapper().selectUserFollowerByPage(
                dto.getUserId(), PageParam.getInstance(dto.getPageNum(), dto.getPageSize()));
    }

    @Override
    public Integer getFollowNum(Long userId) {
        if(redisClient.hasKey(RedisConstant.userStatisticKey + userId)){
            return (Integer) redisClient.hGet(RedisConstant.userStatisticKey + userId,
                    RedisConstant.userFollowNumKey);
        }else{
            return 0;
        }
    }

    @Override
    public Integer getFollowerNum(Long userId) {
        if(redisClient.hasKey(RedisConstant.userStatisticKey + userId)){
            return (Integer) redisClient.hGet(RedisConstant.userStatisticKey + userId,
                    RedisConstant.userFollowerNumKey);
        }else{
            return 0;
        }
    }

    @Override
    public void countUserFollow() {
        List<GetUserFollowCountVo> list = userFollowDao.getBaseMapper().selectUserFollowCount();
        for(GetUserFollowCountVo dto: list){
            redisClient.hSet(RedisConstant.userStatisticKey + dto.getUserId(),
                    RedisConstant.userFollowNumKey, dto.getCnt());
        }
        List<GetUserFollowCountVo> list2 = userFollowDao.getBaseMapper().selectUserFollowerCount();
        for(GetUserFollowCountVo dto: list2){
            redisClient.hSet(RedisConstant.userStatisticKey + dto.getUserId(),
                    RedisConstant.userFollowerNumKey, dto.getCnt());
        }
    }

    // 每次启动项目时，统计当前所有用户的关注数和粉丝数，保存到redis
    @PostConstruct
    public void init() {
        this.countUserFollow();
    }
}
