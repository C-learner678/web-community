package com.jlu.webcommunity.entity.vo.post;

import lombok.Data;

import java.util.Date;

@Data
public class GetPostByPageVo {
    private Long id;
    private Long userId;
    private Long sectionId;
    private String userFrontName;
    private String sectionName;
    private String title;
    private Date createTime;
    private Date updateTime;
}
