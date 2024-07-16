package com.jlu.webcommunity.aop.checkAdmin;

import com.jlu.webcommunity.ehandler.NotAdminException;
import com.jlu.webcommunity.ehandler.NotLoginException;
import com.jlu.webcommunity.filter.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CheckAdminAspect {
    @Pointcut("@annotation(CheckAdmin) || @within(CheckAdmin)")
    public void getCheckAdminAnnotation() {
    }

    @Before("getCheckAdminAnnotation()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        if (UserContext.getUserData() == null || UserContext.getUserData().getId() == null){
            throw new NotLoginException();
        }
        if (UserContext.getUserData().getRole() == null || !UserContext.getUserData().getRole().equals("admin")){
            throw new NotAdminException();
        }
    }
}
