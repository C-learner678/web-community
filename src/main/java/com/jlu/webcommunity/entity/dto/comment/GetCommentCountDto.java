package com.jlu.webcommunity.entity.dto.comment;

import lombok.Data;

@Data
public class GetCommentCountDto {
    private Long postId;
    private Long userId;
}
