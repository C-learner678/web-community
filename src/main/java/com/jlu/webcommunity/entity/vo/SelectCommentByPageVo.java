package com.jlu.webcommunity.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SelectCommentByPageVo {
    private Long id;
    private Long userId;
    private Long postId;
    private String frontName;
    private String avatar;
    private String content;
    private Date createTime;
    private Date updateTime;
}
