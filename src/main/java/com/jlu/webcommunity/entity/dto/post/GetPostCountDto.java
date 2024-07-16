package com.jlu.webcommunity.entity.dto.post;

import lombok.Data;

@Data
public class GetPostCountDto {
    private Long userId;
    private Long sectionId;
    private Boolean top;
}
