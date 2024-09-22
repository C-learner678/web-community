package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.vo.score.GetScoresVO;

import java.util.List;

public interface ScoreService {
    void addScore(Long userId, Integer score);
    void clearDailyScore();
    List<GetScoresVO> getScores(int n);
    Integer getScore(Long userId);
}
