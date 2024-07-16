package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.controller.result.Result;
import com.jlu.webcommunity.entity.dto.userFollow.*;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.service.UserFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
    @Autowired
    private UserFollowService userFollowService;

    // 查看用户A是否已经关注用户B
    @PostMapping("/isFollow")
    public Result isFollow(@RequestBody IsFollowDto dto){
        return Result.success(userFollowService.isFollow(dto.getUserId(), dto.getFollowUserId()));
    }

    // 添加当前用户对user的关注
    @CheckLogin
    @PostMapping("/addFollow")
    public Result addFollow(@RequestBody AddFollowDto dto){
        if(userFollowService.addFollow(dto.getUserId())) {
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.ADD_FOLLOW_FAILED);
        }
    }

    // 取消当前用户对user的关注
    @CheckLogin
    @PostMapping("/removeFollow")
    public Result removeFollow(@RequestBody RemoveFollowDto dto){
        if(userFollowService.removeFollow(dto.getUserId())) {
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.REMOVE_FOLLOW_FAILED);
        }
    }

    // 查看用户关注对象
    @PostMapping("/getFollowByPage")
    public Result getFollowByPage(@RequestBody GetUserFollowByPageDto dto){
        return Result.success(userFollowService.getFollow(dto));
    }

    // 查看用户粉丝
    @PostMapping("/getFollowerByPage")
    public Result getFollowerByPage(@RequestBody GetUserFollowerByPageDto dto){
        return Result.success(userFollowService.getFollower(dto));
    }

    // 获取用户关注数
    @PostMapping("/getFollowNum")
    public Result getFollowNum(@RequestBody GetFollowNumDto dto){
        return Result.success(userFollowService.getFollowNum(dto.getUserId()));
    }

    // 获取用户粉丝数
    @PostMapping("/getFollowerNum")
    public Result getFollowerNum(@RequestBody GetFollowerNumDto dto){
        return Result.success(userFollowService.getFollowerNum(dto.getUserId()));
    }
}
