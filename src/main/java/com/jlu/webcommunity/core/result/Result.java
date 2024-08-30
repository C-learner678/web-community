package com.jlu.webcommunity.core.result;

import com.jlu.webcommunity.enums.StatusCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private Status status;
    private Object result;

    public Result(Status status, Object result){
        this.status = status;
        this.result = result;
    }

    public static Result success(){
        Status status = new Status(StatusCodeEnum.SUCCESS);
        return new Result(status, null);
    }

    public static Result success(Object t){
        Status status = new Status(StatusCodeEnum.SUCCESS);
        return new Result(status, t);
    }

    public static Result fail(StatusCodeEnum statusCodeEnum){
        Status status = new Status(statusCodeEnum);
        return new Result(status, null);
    }
}
