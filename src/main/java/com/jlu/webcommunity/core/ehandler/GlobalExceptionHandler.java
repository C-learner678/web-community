package com.jlu.webcommunity.core.ehandler;

import com.jlu.webcommunity.enums.StatusCodeEnum;
import com.jlu.webcommunity.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = NotLoginException.class)
    public Result handleException(NotLoginException e) {
        return Result.fail(StatusCodeEnum.USER_STATUS_ERROR);
    }

    @ExceptionHandler(value = NotAdminException.class)
    public Result handleException(NotAdminException e) {
        return Result.fail(StatusCodeEnum.ROLE_STATUS_ERROR);
    }

    @ExceptionHandler(value = UserBannedException.class)
    public Result handleException(UserBannedException e) {
        return Result.fail(StatusCodeEnum.USER_BANNED);
    }

    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return Result.fail(StatusCodeEnum.ERROR);
    }
}
