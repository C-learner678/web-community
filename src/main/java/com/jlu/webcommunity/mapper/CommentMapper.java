package com.jlu.webcommunity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jlu.webcommunity.entity.Comment;
import com.jlu.webcommunity.entity.vo.comment.GetCommentByPageVo;
import com.jlu.webcommunity.core.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    List<GetCommentByPageVo> selectCommentByPage
            (@Param("postId") Long postId, @Param("userId") Long userId,
             @Param("pageParam") PageParam pageParam);
    Integer selectCommentCount
            (@Param("postId") Long postId, @Param("userId") Long userId);
}
