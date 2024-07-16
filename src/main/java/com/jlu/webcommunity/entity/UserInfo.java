package com.jlu.webcommunity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_info")
public class UserInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String frontName;
    private String info;
    private String avatar;
    private Boolean deleted;
    private Date createTime;
    private Date updateTime;
}
