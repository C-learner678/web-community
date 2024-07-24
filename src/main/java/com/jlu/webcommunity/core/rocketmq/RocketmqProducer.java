package com.jlu.webcommunity.core.rocketmq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RocketmqProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 同步发送消息
    public SendResult syncSend(Object body, String topicName) {
        return rocketMQTemplate.syncSend(topicName, body);
    }

    // 异步发送消息
    public void asyncSend(Object body, SendCallback callback, String topicName) {
        rocketMQTemplate.asyncSend(topicName, body, callback);
    }

    // oneway 发送消息
    public void onewaySend(Object body, String topicName) {
        rocketMQTemplate.sendOneWay(topicName, body);
    }
}
