package com.jlu.webcommunity.entity.dto.user;

import lombok.Data;

@Data
public class ModifyUserStatusDto {
    private Long userId;
    private Boolean ban;
}
