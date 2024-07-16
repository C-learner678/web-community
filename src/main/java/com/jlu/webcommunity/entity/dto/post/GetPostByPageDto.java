package com.jlu.webcommunity.entity.dto.post;

import lombok.Data;

@Data
public class GetPostByPageDto {
    private Long userId;
    private Long sectionId;
    private Boolean top;
    private int pageSize;
    private int pageNum;
}
