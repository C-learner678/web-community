package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jlu.webcommunity.core.PageParam;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.dao.NotifyMessageDao;
import com.jlu.webcommunity.entity.NotifyMessage;
import com.jlu.webcommunity.entity.dto.notifyMessage.GetNotifyMessageByPageDto;
import com.jlu.webcommunity.entity.dto.notifyMessage.GetNotifyMessageCountDto;
import com.jlu.webcommunity.entity.vo.notifyMessage.GetNotifyMessageByPageVo;
import com.jlu.webcommunity.service.NotifyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 这个类用来让前端主动获取消息
@Slf4j
@Service
public class NotifyMessageServiceImpl implements NotifyMessageService {
    @Autowired
    private NotifyMessageDao notifyMessageDao;

    @Override
    public List<GetNotifyMessageByPageVo> getNotifyMessageByPage(GetNotifyMessageByPageDto dto) {
        List<GetNotifyMessageByPageVo> list = notifyMessageDao.getBaseMapper().getNotifyMessageByPage
                (UserContext.getUserData().getId(), PageParam.getInstance(dto.getPageNum(), dto.getPageSize()));
        UpdateWrapper<NotifyMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("viewed", true);
        updateWrapper.eq("user_id", UserContext.getUserData().getId());
        notifyMessageDao.update(updateWrapper);
        return list;
    }

    @Override
    public int getNotifyMessageCount(GetNotifyMessageCountDto dto) {
        return notifyMessageDao.getBaseMapper().getNotifyMessageCount(UserContext.getUserData().getId(), dto.getViewed());
    }
}
