package com.jlu.webcommunity.entity.dto.comment;

import lombok.Data;

@Data
public class AddCommentDto {
    private Long postId;
    private String content;
}
