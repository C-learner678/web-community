package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.dto.post.*;
import com.jlu.webcommunity.entity.vo.post.GetPostByPageVo;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post getPost(Long id);
    Long addPost(AddPostDto addPostDto);
    boolean modifyPost(ModifyPostDto modifyPostDto);
    boolean deletePost(Long id);
    List<GetPostByPageVo> getPostByPage(GetPostByPageDto dto);
    Integer getPostCount(GetPostCountDto dto);
    boolean modifyPostTop(ModifyPostTopDto dto);
    boolean adminDeletePost(Long id);
    Integer getSearchPostCount(GetSearchPostCountDto dto) throws IOException;
    List<GetPostByPageVo> getSearchPostByPage(GetSearchPostByPageDto dto) throws IOException;
}
