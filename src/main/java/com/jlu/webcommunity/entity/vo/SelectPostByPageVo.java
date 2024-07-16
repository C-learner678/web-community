package com.jlu.webcommunity.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SelectPostByPageVo {
    private Long id;
    private Long userId;
    private Long sectionId;
    private String userFrontName;
    private String sectionName;
    private String title;
    private Date createTime;
    private Date updateTime;
}
