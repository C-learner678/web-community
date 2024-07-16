package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.UserFollow;
import com.jlu.webcommunity.mapper.UserFollowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserFollowDao extends ServiceImpl<UserFollowMapper, UserFollow> {
}
