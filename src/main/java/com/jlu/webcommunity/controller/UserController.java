package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkAdmin.CheckAdmin;
import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.entity.dto.user.*;
import com.jlu.webcommunity.entity.vo.LoginVo;
import com.jlu.webcommunity.entity.vo.RegisterVo;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.service.UserService;
import com.jlu.webcommunity.controller.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDto loginDto){
        LoginVo result = userService.login(loginDto, true);
        if(result.getStatus() == 0) {
            return Result.success(result.getToken());
        }else if(result.getStatus() == 2){
            return Result.fail(StatusCodeEnum.OLD_CAPTCHA_ERROR);
        }else if(result.getStatus() == 3){
            return Result.fail(StatusCodeEnum.WRONG_CAPTCHA_ERROR);
        }else{
            return Result.fail(StatusCodeEnum.LOGIN_ERROR);
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDto registerDto){
        RegisterVo result = userService.register(registerDto);
        if(result.getStatus() == 0) {
            return Result.success(result.getToken());
        }else if(result.getStatus() == 2){
            return Result.fail(StatusCodeEnum.OLD_CAPTCHA_ERROR);
        }else if(result.getStatus() == 3){
            return Result.fail(StatusCodeEnum.WRONG_CAPTCHA_ERROR);
        }else{
            return Result.fail(StatusCodeEnum.REGISTER_ERROR);
        }
    }

    @PostMapping("/logout")
    public Result logout(){
        userService.logout();
        return Result.success();
    }

    @PostMapping("/getCaptcha")
    public Result getCaptcha(){
        return Result.success(userService.getCaptcha());
    }

    @PostMapping("/getCurrentUser")
    public Result getCurrentUser(){
        if(UserContext.getUserData() == null || UserContext.getUserData().getId() == null){
            return Result.success(); //未登录则返回空
        }
        return Result.success(userService.getCurrentUser());
    }

    // 测试接口：快速登录
    @PostMapping("/quickAdminLogin")
    public Result quickAdminLogin(){
        LoginDto loginDto = new LoginDto();
        loginDto.setName("admin");
        loginDto.setPassword("123456");
        LoginVo result = userService.login(loginDto, false);
        return Result.success(result.getToken());
    }

    @CheckLogin
    @PostMapping("/modifyPassword")
    public Result modifyPassword(@RequestBody ModifyPasswordDto modifyPasswordDto){
        if(userService.modifyPassword(modifyPasswordDto)) {
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.MODIFY_PASSWORD_ERROR);
        }
    }

    // 管理员封禁/解封用户
    @CheckAdmin
    @PostMapping("/modifyUserStatus")
    public Result modifyUserStatus(@RequestBody ModifyUserStatusDto modifyUserStatusDto){
        if(userService.modifyUserStatus(modifyUserStatusDto)){
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.MODIFY_USER_STATUS_FAILED);
        }
    }

    @PostMapping("/getUserBanned")
    public Result getUserBanned(@RequestBody GetUserBannedDto getUserBannedDto){
        return Result.success(userService.isUserBanned(getUserBannedDto.getUserId()));
    }
}
