package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.entity.UserInfo;

public interface UserInfoMapper extends BaseMapper<UserInfo> {
    int insertUserInfo(UserInfo userInfo);
}
