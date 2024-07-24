package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.UserFoot;
import com.jlu.webcommunity.mapper.UserFootMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserFootDao extends ServiceImpl<UserFootMapper, UserFoot> {
}
