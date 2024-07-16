package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.entity.User;

public interface UserMapper extends BaseMapper<User> {
    int insertUser(User user);
}
