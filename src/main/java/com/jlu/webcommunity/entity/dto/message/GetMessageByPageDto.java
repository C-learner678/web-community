package com.jlu.webcommunity.entity.dto.message;

import lombok.Data;

@Data
public class GetMessageByPageDto {
    private int pageSize;
    private int pageIndex;
}
