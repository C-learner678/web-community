package com.jlu.webcommunity.entity.vo.score;

import lombok.Data;

@Data
public class GetScoresVO {
    private Long userId;
    private Integer score;
    private String frontName;
}
