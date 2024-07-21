package com.jlu.webcommunity.entity.dto.post;

import lombok.Data;

@Data
public class GetSearchPostByPageDto {
    private String text;
    private Long sectionId;
    private int pageSize;
    private int pageNum;
}
