package com.jlu.webcommunity.core.rocketmq;

import com.jlu.webcommunity.core.constant.MessageTypeConstant;
import com.jlu.webcommunity.core.constant.RocketmqConstant;
import com.jlu.webcommunity.service.NotifyMessageService;
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
        consumerGroup = "CONSUMER_GROUP_NOTIFY_" + RocketmqConstant.topic
)
public class RocketmqNotifyConsumer implements RocketMQListener<RocketmqBody> {
    @Autowired
    private NotifyMessageService notifyMessageService;

    @Override
    public void onMessage(RocketmqBody body) {
        if (Objects.equals(body.getType(), MessageTypeConstant.ADD_USER_FOLLOW)) {
            notifyMessageService.notifyAddUserFollow(body.getUserId(), body.getFromUserId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.ADD_LIKE_POST)){
            notifyMessageService.notifyAddLikePost(body.getFromUserId(), body.getRelateId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.ADD_COMMENT)){
            notifyMessageService.notifyAddComment(body.getFromUserId(), body.getRelateId(), body.getMessage());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.BAN_USER)){
            notifyMessageService.notifyBanUser(body.getUserId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.CANCEL_BAN_USER)){
            notifyMessageService.notifyCancelBanUser(body.getUserId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.ADD_LIKE_COMMENT)){
            notifyMessageService.notifyAddLikeComment(body.getUserId(), body.getRelateId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.DELETE_POST)){
            notifyMessageService.notifyDeletePost(body.getUserId(), body.getRelateId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.DELETE_COMMENT)){
            notifyMessageService.notifyDeleteComment(body.getUserId(), body.getRelateId());
        }
    }
}
