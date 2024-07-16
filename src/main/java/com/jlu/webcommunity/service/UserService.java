package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.dto.user.LoginDto;
import com.jlu.webcommunity.entity.dto.user.ModifyPasswordDto;
import com.jlu.webcommunity.entity.dto.user.ModifyUserStatusDto;
import com.jlu.webcommunity.entity.dto.user.RegisterDto;
import com.jlu.webcommunity.entity.vo.CaptchaVo;
import com.jlu.webcommunity.entity.vo.GetCurrentUserVo;
import com.jlu.webcommunity.entity.vo.LoginVo;
import com.jlu.webcommunity.entity.vo.RegisterVo;

public interface UserService {
    LoginVo login(LoginDto loginDto, boolean useCaptcha);
    RegisterVo register(RegisterDto registerDto);
    void logout();
    CaptchaVo getCaptcha();
    boolean modifyPassword(ModifyPasswordDto modifyPasswordDto);
    GetCurrentUserVo getCurrentUser();
    boolean modifyUserStatus(ModifyUserStatusDto modifyUserStatusDto);
    boolean isUserBanned(Long id);
    public void setAllUserStatus();
}
