package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.core.PageParam;
import com.jlu.webcommunity.entity.NotifyMessage;
import com.jlu.webcommunity.entity.vo.notifyMessage.GetNotifyMessageByPageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotifyMessageMapper extends BaseMapper<NotifyMessage> {
    List<GetNotifyMessageByPageVo> getNotifyMessageByPage
            (@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);
    Integer getNotifyMessageCount
            (@Param("userId") Long userId, @Param("viewed") Boolean viewed);
}
