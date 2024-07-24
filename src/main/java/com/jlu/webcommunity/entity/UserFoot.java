package com.jlu.webcommunity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_foot")
public class UserFoot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long postId;
    private Boolean isPost;
    private Boolean readStatus;
    private Boolean likeStatus;
    private Boolean collectStatus;
    private Boolean deleted;
    private Date createTime;
    private Date updateTime;
}
