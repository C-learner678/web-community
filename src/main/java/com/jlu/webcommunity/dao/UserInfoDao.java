package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.UserInfo;
import com.jlu.webcommunity.mapper.UserInfoMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoDao extends ServiceImpl<UserInfoMapper, UserInfo> {
}
