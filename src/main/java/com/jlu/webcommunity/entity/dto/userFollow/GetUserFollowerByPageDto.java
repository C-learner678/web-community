package com.jlu.webcommunity.entity.dto.userFollow;

import lombok.Data;

@Data
public class GetUserFollowerByPageDto {
    private Long userId;
    private int pageSize;
    private int pageNum;
}
