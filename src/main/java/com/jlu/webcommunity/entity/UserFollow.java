package com.jlu.webcommunity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_follow")
public class UserFollow {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId; //被关注者
    private Long followUserId; //关注者
    private Boolean deleted;
    private Date createTime;
    private Date updateTime;
}
