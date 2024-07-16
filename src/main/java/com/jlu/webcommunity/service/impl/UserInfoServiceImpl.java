package com.jlu.webcommunity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jlu.webcommunity.dao.UserInfoDao;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.entity.dto.userInfo.ModifyAvatarDto;
import com.jlu.webcommunity.entity.dto.userInfo.ModifyUserInfoDto;
import com.jlu.webcommunity.filter.context.UserContext;
import com.jlu.webcommunity.service.UserInfoService;
import com.jlu.webcommunity.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public UserInfo getUserInfo(Long userId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("deleted", false);
        UserInfo userInfo = userInfoDao.getOne(wrapper);
        return userInfo;
    }

    @Override
    public boolean modifyUserInfo(ModifyUserInfoDto dto) {
        Long userId = UserContext.getUserData().getId();
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoDao.getOne(queryWrapper);
        userInfo.setFrontName(dto.getFrontName());
        userInfo.setInfo(dto.getInfo());
        userInfo.setUpdateTime(new Date());
        userInfoDao.updateById(userInfo);
        return true;
    }

    @Override
    public boolean modifyAvatar(ModifyAvatarDto dto){
        Long userId = UserContext.getUserData().getId();
        String avatar = dto.getAvatar();
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserInfo userInfo = userInfoDao.getOne(queryWrapper);
        userInfo.setAvatar(avatar);
        userInfoDao.updateById(userInfo);
        return true;
    }
}
