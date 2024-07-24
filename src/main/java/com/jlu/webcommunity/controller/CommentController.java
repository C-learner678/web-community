package com.jlu.webcommunity.controller;

import com.jlu.webcommunity.core.aop.checkAdmin.CheckAdmin;
import com.jlu.webcommunity.core.aop.checkBanned.CheckBanned;
import com.jlu.webcommunity.core.aop.checkLogin.CheckLogin;
import com.jlu.webcommunity.controller.result.Result;
import com.jlu.webcommunity.entity.dto.comment.AddCommentDto;
import com.jlu.webcommunity.entity.dto.comment.DeleteCommentDto;
import com.jlu.webcommunity.entity.dto.comment.GetCommentByPageDto;
import com.jlu.webcommunity.entity.dto.comment.GetCommentCountDto;
import com.jlu.webcommunity.entity.vo.GetCommentByPageVo;
import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/getCommentByPage")
    public Result getCommentByPage(@RequestBody GetCommentByPageDto dto){
        List<GetCommentByPageVo> comments = commentService.getCommentByPage(dto);
        return Result.success(comments);
    }

    @PostMapping("/getCommentCount")
    public Result getCommentCount(@RequestBody GetCommentCountDto dto){
        int count = commentService.getCommentCount(dto);
        return Result.success(count);
    }

    @CheckLogin
    @CheckBanned
    @PostMapping("/addComment")
    public Result addComment(@RequestBody AddCommentDto dto){
        if(commentService.addComment(dto)){
            return Result.success();
        }else {
            return Result.fail(StatusCodeEnum.ADD_COMMENT_FAILED);
        }
    }

    @CheckAdmin
    @PostMapping("/deleteComment")
    public Result deleteComment(@RequestBody DeleteCommentDto dto){
        if(commentService.deleteComment(dto.getCommentId())){
            return Result.success();
        }else{
            return Result.fail(StatusCodeEnum.DELETE_COMMENT_FAILED);
        }
    }
}
