package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.core.result.Result;
import com.jlu.webcommunity.entity.dto.notifyMessage.GetNotifyMessageByPageDto;
import com.jlu.webcommunity.entity.dto.notifyMessage.GetNotifyMessageCountDto;
import com.jlu.webcommunity.service.NotifyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notifyMessage")
public class NotifyMessageController {
    @Autowired
    private NotifyMessageService notifyMessageService;

    @CheckLogin
    @PostMapping("/getNotifyMessageByPage")
    public Result getNotifyMessageByPage(@RequestBody GetNotifyMessageByPageDto dto){
        return Result.success(notifyMessageService.getNotifyMessageByPage(dto));
    }

    @CheckLogin
    @PostMapping("/getNotifyMessageCount")
    public Result getNotifyMessageCount(@RequestBody GetNotifyMessageCountDto dto){
        return Result.success(notifyMessageService.getNotifyMessageCount(dto));
    }
}

