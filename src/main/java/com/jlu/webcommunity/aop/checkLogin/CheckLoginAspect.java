package com.jlu.webcommunity.aop.checkLogin;

import com.jlu.webcommunity.ehandler.NotLoginException;
import com.jlu.webcommunity.filter.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
@Component
public class CheckLoginAspect {
    @Pointcut("@annotation(CheckLogin) || @within(CheckLogin)")
    public void getCheckLoginAnnotation() {
    }

    @Before("getCheckLoginAnnotation()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (UserContext.getUserData() == null || UserContext.getUserData().getId() == null){
            throw new NotLoginException();
        }
    }
}
