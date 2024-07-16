package com.jlu.webcommunity.entity.dto.post;

import lombok.Data;

@Data
public class ModifyPostDto {
    private Long postId;
    private String title;
    private String content;
}
