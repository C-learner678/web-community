package com.jlu.webcommunity.entity.vo.comment;

import lombok.Data;

import java.util.Date;

@Data
public class GetCommentByPageVo {
    private Long id;
    private Long userId;
    private Long postId;
    private String frontName;
    private String avatar;
    private String content;
    private Date createTime;
    private Date updateTime;
    private Integer likeCnt;
    private Boolean likeStatus;
}
