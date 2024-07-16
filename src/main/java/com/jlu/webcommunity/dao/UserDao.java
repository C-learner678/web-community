package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.User;
import com.jlu.webcommunity.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserDao extends ServiceImpl<UserMapper, User> {

}
