package com.jlu.webcommunity.entity.dto.post;

import lombok.Data;

@Data
public class GetSearchPostCountDto {
    private String text;
    private Long sectionId;
}
