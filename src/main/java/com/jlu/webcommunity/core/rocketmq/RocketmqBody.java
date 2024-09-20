package com.jlu.webcommunity.core.rocketmq;

import lombok.Data;

@Data
public class RocketmqBody {
    private Long userId;
    private Long fromUserId;
    private Long postId;
    private Long commentId;
    private Integer type;
}
