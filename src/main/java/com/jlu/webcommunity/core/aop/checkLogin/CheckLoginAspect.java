package com.jlu.webcommunity.core.aop.checkLogin;

import com.jlu.webcommunity.core.ehandler.NotLoginException;
import com.jlu.webcommunity.core.filter.context.UserContext;
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
    @Pointcut("@annotation(com.jlu.webcommunity.core.aop.checkLogin.CheckLogin) || @within(com.jlu.webcommunity.core.aop.checkLogin.CheckLogin)")
    public void getCheckLoginAnnotation() {
    }

    @Before("getCheckLoginAnnotation()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (UserContext.getUserData() == null || UserContext.getUserData().getId() == null){
            throw new NotLoginException();
        }
    }
}
