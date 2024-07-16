package com.jlu.webcommunity.entity.dto.comment;

import lombok.Data;

@Data
public class GetCommentByPageDto {
    private Long postId;
    private Long userId;
    private int pageSize;
    private int pageNum;
}
