package com.jlu.webcommunity.entity.vo;

import lombok.Data;

@Data
public class GetCurrentUserVo {
    private Long userId;
    private String name;
    private String role;
    private String frontName;
    private String info;
    private String avatar;
}
