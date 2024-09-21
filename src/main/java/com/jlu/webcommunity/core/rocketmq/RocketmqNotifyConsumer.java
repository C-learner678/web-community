package com.jlu.webcommunity.core.rocketmq;

import com.jlu.webcommunity.constant.MessageTypeConstant;
import com.jlu.webcommunity.constant.RocketmqConstant;
import com.jlu.webcommunity.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
    private MqMessageService notifyMessageService;

    @Override
    public void onMessage(RocketmqBody body) {
        if (Objects.equals(body.getType(), MessageTypeConstant.ADD_USER_FOLLOW)) {
            notifyMessageService.notifyAddUserFollow(body.getUserId(), body.getFromUserId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.ADD_LIKE_POST)){
            notifyMessageService.notifyAddLikePost(body.getFromUserId(), body.getPostId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.ADD_COMMENT)){
            notifyMessageService.notifyAddComment(body.getFromUserId(), body.getPostId(), body.getCommentId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.BAN_USER)){
            notifyMessageService.notifyBanUser(body.getUserId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.CANCEL_BAN_USER)){
            notifyMessageService.notifyCancelBanUser(body.getUserId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.ADD_LIKE_COMMENT)){
            notifyMessageService.notifyAddLikeComment(body.getFromUserId(), body.getCommentId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.DELETE_POST)){
            notifyMessageService.notifyDeletePost(body.getUserId(), body.getPostId());
        }else if(Objects.equals(body.getType(), MessageTypeConstant.DELETE_COMMENT)){
            notifyMessageService.notifyDeleteComment(body.getUserId(), body.getPostId(), body.getCommentId());
        }
    }
}
