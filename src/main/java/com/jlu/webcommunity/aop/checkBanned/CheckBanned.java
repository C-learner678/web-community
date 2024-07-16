package com.jlu.webcommunity.aop.checkBanned;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckBanned {

}
