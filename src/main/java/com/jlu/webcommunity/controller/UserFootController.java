package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.core.result.Result;
import com.jlu.webcommunity.entity.dto.userFoot.GetCollectPostByPageDto;
import com.jlu.webcommunity.entity.dto.userFoot.GetPostUserFootCountDto;
import com.jlu.webcommunity.entity.dto.userFoot.GetPostUserFootDto;
import com.jlu.webcommunity.entity.dto.userFoot.ModifyPostUserFootDto;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.service.UserFootService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/userFoot")
public class UserFootController {
    @Autowired
    private UserFootService userFootService;

    @PostMapping("/getPostUserFoot")
    public Result getPostUserFoot(@RequestBody GetPostUserFootDto dto){
        return Result.success(userFootService.getPostUserFoot(dto));
    }

    @CheckLogin
    @PostMapping("/modifyPostUserFoot")
    public Result modifyPostUserFoot(@RequestBody ModifyPostUserFootDto dto){
        if(userFootService.modifyPostUserFoot(dto)) {
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.POST_NOT_EXIST);
        }
    }

    @PostMapping("/getPostUserFootCount")
    public Result getPostUserFootCount(@RequestBody GetPostUserFootCountDto dto){
        return Result.success(userFootService.getPostUserFootCount(dto));
    }

    @CheckLogin
    @PostMapping("/getCollectPostByPage")
    public Result getCollectPostByPage(@RequestBody GetCollectPostByPageDto dto){
        return Result.success(userFootService.getCollectPostByPage(dto));
    }
}
