package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.dto.notifyMessage.GetNotifyMessageByPageDto;
import com.jlu.webcommunity.entity.dto.notifyMessage.GetNotifyMessageCountDto;
import com.jlu.webcommunity.entity.vo.notifyMessage.GetNotifyMessageByPageVo;

import java.util.List;

public interface NotifyMessageService {
    List<GetNotifyMessageByPageVo> getNotifyMessageByPage(GetNotifyMessageByPageDto dto);
    int getNotifyMessageCount(GetNotifyMessageCountDto dto);
}
