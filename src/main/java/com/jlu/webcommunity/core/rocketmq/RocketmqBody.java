package com.jlu.webcommunity.core.rocketmq;

import lombok.Data;

@Data
public class RocketmqBody {
    private Long userId;
    private Long fromUserId;
    private Long relateId;
    private Integer type;
    private String message;
}
