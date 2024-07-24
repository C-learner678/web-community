package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.NotifyMessage;
import com.jlu.webcommunity.mapper.NotifyMessageMapper;
import org.springframework.stereotype.Repository;

@Repository
public class NotifyMessageDao extends ServiceImpl<NotifyMessageMapper, NotifyMessage> {
}
