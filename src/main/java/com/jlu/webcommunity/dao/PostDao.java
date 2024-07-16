package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.mapper.PostMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class PostDao extends ServiceImpl<PostMapper, Post> {

}
