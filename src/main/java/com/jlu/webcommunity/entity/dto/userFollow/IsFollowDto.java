package com.jlu.webcommunity.entity.dto.userFollow;

import lombok.Data;

@Data
public class IsFollowDto {
    private Long userId;
    private Long followUserId;
}
