package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.dto.post.*;
import com.jlu.webcommunity.entity.vo.SelectPostByPageVo;

import java.util.List;

public interface PostService {
    Post getPost(Long id);
    Long addPost(AddPostDto addPostDto);
    boolean modifyPost(ModifyPostDto modifyPostDto);
    boolean deletePost(Long id);
    List<SelectPostByPageVo> getPostByPage(GetPostByPageDto dto);
    Integer getPostCount(GetPostCountDto dto);
    boolean modifyPostTop(ModifyPostTopDto dto);
    boolean adminDeletePost(Long id);
}
