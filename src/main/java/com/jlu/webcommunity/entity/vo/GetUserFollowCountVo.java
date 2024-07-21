package com.jlu.webcommunity.entity.vo;

import lombok.Data;

@Data
public class GetUserFollowCountVo {
    private Long userId;
    private Integer cnt;
}
