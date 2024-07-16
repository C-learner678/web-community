package com.jlu.webcommunity.entity.dto.post;

import lombok.Data;

@Data
public class AddPostDto {
    private Long sectionId;
    private String title;
    private String content;
}
