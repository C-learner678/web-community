package com.jlu.webcommunity.entity.vo.notifyMessage;

import lombok.Data;

import java.util.Date;

@Data
public class GetNotifyMessageByPageVo {
    private Integer type;
    private Long fromUserId;
    private String fromUserName;
    private Long postId;
    private String postTitle;
    private Long commentId;
    private String commentContent;
    private Date createTime;
}
