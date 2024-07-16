package com.jlu.webcommunity.controller.result;

import com.jlu.webcommunity.enums.StatusCodeEnum;
import lombok.Data;

@Data
public class Status {
    private int code;
    private String msg;
    public Status(StatusCodeEnum statusCodeEnum){
        this.code = statusCodeEnum.getCode();
        this.msg = statusCodeEnum.getMsg();
    }
}
