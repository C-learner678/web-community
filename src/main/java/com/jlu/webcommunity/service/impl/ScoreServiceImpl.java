package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.core.RedisClient;
import com.jlu.webcommunity.dao.UserDao;
import com.jlu.webcommunity.dao.UserInfoDao;
import com.jlu.webcommunity.entity.User;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.vo.score.GetScoresVO;
import com.jlu.webcommunity.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private RedisClient redisClient;

    private final static Integer dailyMaxScore = 10;

    @Override
    public void addScore(Long userId, Integer score) {
        Integer s = (Integer) redisClient.hGet(RedisConstant.userStatisticKey + userId, RedisConstant.dailyScoreKey);
        if(s == null){
            s = 0;
        }
        // 如果超过每日上限，则最多只增加到每日上限
        if(s + score > dailyMaxScore){
            score = dailyMaxScore - s;
        }
        redisClient.zIncr(RedisConstant.scoreKey, userId + "", score * 1.0);
        redisClient.hIncr(RedisConstant.userStatisticKey + userId, RedisConstant.dailyScoreKey, score.longValue());
    }

    @Override
    public void clearDailyScore() {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.select("id");
        List<Object> ids = userDao.getBaseMapper().selectObjs(wrapper);
        for(Object id: ids){
            redisClient.hSet(RedisConstant.userStatisticKey + (long) id, RedisConstant.dailyScoreKey, 0.0);
        }
    }

    @Override
    public List<GetScoresVO> getScores(int n){
        List<ImmutablePair<String, Double>> list = redisClient.zTopNScore(RedisConstant.scoreKey, n);
        List<Long> ids = list.stream().map(i -> Long.parseLong(i.getLeft())).collect(Collectors.toList());
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_id", ids);
        queryWrapper.select("front_name");
        List<Object> names = userInfoDao.getBaseMapper().selectObjs(queryWrapper);
        List<GetScoresVO> result = new ArrayList<>();
        for(int i = 0; i < ids.size(); ++i){
            GetScoresVO vo = new GetScoresVO();
            vo.setFrontName((String) names.get(i));
            vo.setUserId(ids.get(i));
            vo.setScore(list.get(i).getRight().intValue());
            result.add(vo);
        }
        return result;
    }

    @Override
    public Integer getScore(Long userId){
        Double result = redisClient.zScore(RedisConstant.scoreKey, userId + "");
        if(result == null){
            return 0;
        }else{
            return result.intValue();
        }
    }
}
