package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.result.Result;
import com.jlu.webcommunity.entity.dto.score.GetScoreDto;
import com.jlu.webcommunity.entity.dto.score.GetScoresDto;
import com.jlu.webcommunity.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    private ScoreService scoreService;

    @PostMapping("/getScores")
    public Result getScores(@RequestBody GetScoresDto dto){
        return Result.success(scoreService.getScores(dto.getN()));
    }

    @PostMapping("getScore")
    public Result getScore(@RequestBody GetScoreDto dto){
        return Result.success(scoreService.getScore(dto.getUserId()));
    }
}
