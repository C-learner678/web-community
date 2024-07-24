package com.jlu.webcommunity.core.aop.checkAdmin;

import com.jlu.webcommunity.core.ehandler.NotAdminException;
import com.jlu.webcommunity.core.ehandler.NotLoginException;
import com.jlu.webcommunity.core.filter.context.UserContext;
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
    @Pointcut("@annotation(com.jlu.webcommunity.core.aop.checkAdmin.CheckAdmin) || @within(com.jlu.webcommunity.core.aop.checkAdmin.CheckAdmin)")
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
