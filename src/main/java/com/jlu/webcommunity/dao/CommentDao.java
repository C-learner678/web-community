package com.jlu.webcommunity.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jlu.webcommunity.entity.Comment;
import com.jlu.webcommunity.mapper.CommentMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends ServiceImpl<CommentMapper, Comment> {
}
