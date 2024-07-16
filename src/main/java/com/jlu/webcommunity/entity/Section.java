package com.jlu.webcommunity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("section")
public class Section {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String info;
    private Boolean deleted;
    private Date createTime;
    private Date updateTime;
}
