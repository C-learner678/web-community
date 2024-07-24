package com.jlu.webcommunity.core.aop.checkBanned;

import com.jlu.webcommunity.core.ehandler.UserBannedException;
import com.jlu.webcommunity.core.filter.context.UserContext;
import com.jlu.webcommunity.service.UserService;
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

    @Pointcut("@annotation(com.jlu.webcommunity.core.aop.checkBanned.CheckBanned) || @within(com.jlu.webcommunity.core.aop.checkBanned.CheckBanned)")
    public void getCheckAdminAnnotation() {
    }

    @Before("getCheckAdminAnnotation()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if(userService.isUserBanned(UserContext.getUserData().getId())){
            throw new UserBannedException();
        }
    }
}
