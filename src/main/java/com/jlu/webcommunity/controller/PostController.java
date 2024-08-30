package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkAdmin.CheckAdmin;
import com.jlu.webcommunity.core.aop.checkBanned.CheckBanned;
import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.entity.Post;
import com.jlu.webcommunity.entity.dto.post.*;
import com.jlu.webcommunity.entity.vo.GetPostByPageVo;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.core.result.Result;
import com.jlu.webcommunity.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/getPost")
    public Result getPost(@RequestBody GetPostDto dto){
        Post post = postService.getPost(dto.getPostId());
        if(post != null){
            return Result.success(post);
        }
        return Result.fail(StatusCodeEnum.POST_NOT_EXIST);
    }

    @CheckLogin
    @CheckBanned
    @PostMapping("/addPost")
    public Result addPost(@RequestBody AddPostDto addPostDto){
        Long id = postService.addPost(addPostDto);
        if(id > 0l){
            return Result.success(id);
        };
        return Result.fail(StatusCodeEnum.ADD_POST_FAILED);
    }

    @CheckLogin
    @CheckBanned
    @PostMapping("/modifyPost")
    public Result modifyPost(@RequestBody ModifyPostDto modifyPostDto){
        if(postService.modifyPost(modifyPostDto)) {
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.MODIFY_POST_FAILED);
        }
    }

    @CheckLogin
    @PostMapping("/deletePost")
    public Result deletePost(@RequestBody DeletePostDto dto){
        if(postService.deletePost(dto.getPostId())) {
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.DELETE_POST_FAILED);
        }
    }

    @PostMapping("/getPostByPage")
    public Result getUserPostByPage(@RequestBody GetPostByPageDto dto){
        List<GetPostByPageVo> posts = postService.getPostByPage(dto);
        return Result.success(posts);
    }

    @PostMapping("/getPostCount")
    public Result getUserPostCount(@RequestBody GetPostCountDto dto){
        int count = postService.getPostCount(dto);
        return Result.success(count);
    }

    @CheckAdmin
    @PostMapping("/modifyPostTop")
    public Result modifyPostTop(@RequestBody ModifyPostTopDto dto){
        if(postService.modifyPostTop(dto)){
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.MODIFY_POST_TOP_FAILED);
        }
    }

    @CheckAdmin
    @PostMapping("/adminDeletePost")
    public Result adminDeletePost(@RequestBody DeletePostDto dto){
        if(postService.adminDeletePost(dto.getPostId())){
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.DELETE_POST_FAILED);
        }
    }

    @PostMapping("/getSearchPostCount")
    public Result getSearchPostCount(@RequestBody GetSearchPostCountDto dto) throws IOException {
        return Result.success(postService.getSearchPostCount(dto));
    }

    @PostMapping("/getSearchPostByPage")
    public Result getSearchPostByPage(@RequestBody GetSearchPostByPageDto dto) throws IOException {
        return Result.success(postService.getSearchPostByPage(dto));
    }
}
