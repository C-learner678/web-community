package com.jlu.webcommunity.aop.checkBanned;

import com.jlu.webcommunity.ehandler.NotAdminException;
import com.jlu.webcommunity.ehandler.NotLoginException;
import com.jlu.webcommunity.ehandler.UserBannedException;
import com.jlu.webcommunity.filter.context.UserContext;
import com.jlu.webcommunity.service.UserService;
import com.jlu.webcommunity.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CheckBannedAspect {
    @Autowired
    private UserService userService;

    @Pointcut("@annotation(CheckBanned) || @within(CheckBanned)")
    public void getCheckAdminAnnotation() {
    }

    @Before("getCheckAdminAnnotation()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if(userService.isUserBanned(UserContext.getUserData().getId())){
            throw new UserBannedException();
        }
    }
}
