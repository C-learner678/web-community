package com.jlu.webcommunity.service;

import com.jlu.webcommunity.entity.dto.comment.AddCommentDto;
import com.jlu.webcommunity.entity.dto.comment.GetCommentByPageDto;
import com.jlu.webcommunity.entity.dto.comment.GetCommentCountDto;
import com.jlu.webcommunity.entity.vo.comment.GetCommentByPageVo;

import java.util.List;

public interface CommentService {
    List<GetCommentByPageVo> getCommentByPage(GetCommentByPageDto dto);
    Integer getCommentCount(GetCommentCountDto dto);
    boolean addComment(AddCommentDto dto);
    boolean deleteComment(Long id);
}
