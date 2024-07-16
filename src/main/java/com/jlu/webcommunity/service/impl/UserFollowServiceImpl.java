package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.dao.UserFollowDao;
import com.jlu.webcommunity.dao.UserInfoDao;
import com.jlu.webcommunity.entity.UserFollow;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.userFollow.GetUserFollowByPageDto;
import com.jlu.webcommunity.entity.vo.SelectUserFollowCountVo;
import com.jlu.webcommunity.entity.dto.userFollow.GetUserFollowerByPageDto;
import com.jlu.webcommunity.filter.context.UserContext;
import com.jlu.webcommunity.service.UserFollowService;
import com.jlu.webcommunity.util.PageParam;
import com.jlu.webcommunity.util.RedisUtil;
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
    private RedisUtil redisUtil;

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
        UserInfo userInfo = userInfoDao.getById(userId);
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
        redisUtil.hIncr(RedisConstant.userStatisticKey + followUserId,
                RedisConstant.userFollowNumKey, 1l);
        redisUtil.hIncr(RedisConstant.userStatisticKey + userId,
                RedisConstant.userFollowerNumKey, 1l);
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
            redisUtil.hIncr(RedisConstant.userStatisticKey + followUserId,
                    RedisConstant.userFollowNumKey, -1l);
            redisUtil.hIncr(RedisConstant.userStatisticKey + userId,
                    RedisConstant.userFollowerNumKey, -1l);
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
        if(redisUtil.hasKey(RedisConstant.userStatisticKey + userId)){
            return (Integer) redisUtil.hGet(RedisConstant.userStatisticKey + userId,
                    RedisConstant.userFollowNumKey);
        }else{
            return 0;
        }
    }

    @Override
    public Integer getFollowerNum(Long userId) {
        if(redisUtil.hasKey(RedisConstant.userStatisticKey + userId)){
            return (Integer) redisUtil.hGet(RedisConstant.userStatisticKey + userId,
                    RedisConstant.userFollowerNumKey);
        }else{
            return 0;
        }
    }

    @Override
    public void countUserFollow() {
        List<SelectUserFollowCountVo> list = userFollowDao.getBaseMapper().selectUserFollowCount();
        for(SelectUserFollowCountVo dto: list){
            redisUtil.hSet(RedisConstant.userStatisticKey + dto.getUserId(),
                    RedisConstant.userFollowNumKey, dto.getCnt());
        }
        List<SelectUserFollowCountVo> list2 = userFollowDao.getBaseMapper().selectUserFollowerCount();
        for(SelectUserFollowCountVo dto: list2){
            redisUtil.hSet(RedisConstant.userStatisticKey + dto.getUserId(),
                    RedisConstant.userFollowerNumKey, dto.getCnt());
        }
    }

    // 每次启动项目时，统计当前所有用户的关注数和粉丝数，保存到redis
    @PostConstruct
    public void init() {
        this.countUserFollow();
    }
}
