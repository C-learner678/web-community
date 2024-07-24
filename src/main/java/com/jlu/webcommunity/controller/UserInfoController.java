package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.controller.result.Result;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.userInfo.GetUserInfoDto;
import com.jlu.webcommunity.entity.dto.userInfo.ModifyAvatarDto;
import com.jlu.webcommunity.entity.dto.userInfo.ModifyUserInfoDto;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/getUserInfo")
    public Result getUserInfo(@RequestBody GetUserInfoDto dto){
        UserInfo userInfo = userInfoService.getUserInfo(dto.getUserId());
        if(userInfo != null) {
            return Result.success(userInfo);
        }else{
            return Result.fail(StatusCodeEnum.USER_NOT_EXIST);
        }
    }

    @CheckLogin
    @PostMapping("/modifyUserInfo")
    public Result modifyUserInfo(@RequestBody ModifyUserInfoDto dto){
        if(userInfoService.modifyUserInfo(dto)){
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.MODIFY_USER_INFO_FAILED);
        }
    }

    @CheckLogin
    @PostMapping("/modifyAvatar")
    public Result modifyAvatar(@RequestBody ModifyAvatarDto dto){
        if(userInfoService.modifyAvatar(dto)){
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.MODIFY_USER_INFO_FAILED);
        }
    }
}
