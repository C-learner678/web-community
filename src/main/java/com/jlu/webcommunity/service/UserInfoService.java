package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.userInfo.ModifyAvatarDto;
import com.jlu.webcommunity.entity.dto.userInfo.ModifyUserInfoDto;

public interface UserInfoService {
    UserInfo getUserInfo(Long userId);
    boolean modifyUserInfo(ModifyUserInfoDto dto);
    boolean modifyAvatar(ModifyAvatarDto dto);
}
