package com.jlu.webcommunity.core.rocketmq;

import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.constant.RedisConstant;
import com.jlu.webcommunity.constant.RocketmqConstant;
import com.jlu.webcommunity.core.RedisClient;
import com.jlu.webcommunity.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = RocketmqConstant.topic,
        consumerGroup = "CONSUMER_GROUP_SCORE_" + RocketmqConstant.topic
)
public class RocketmqScoreConsumer implements RocketMQListener<RocketmqBody> {
    @Autowired
    private ScoreService scoreService;

    @Override
    public void onMessage(RocketmqBody body) {
        if(Objects.equals(body.getType(), MessageTypeConstant.ADD_COMMENT)){ // 发表评论，加两分
            scoreService.addScore(body.getFromUserId(), 2);
        }else if(Objects.equals(body.getType(), MessageTypeConstant.PUBLISH_POST)){ // 发表帖子，加五分
            scoreService.addScore(body.getFromUserId(), 5);
        }
    }
}
