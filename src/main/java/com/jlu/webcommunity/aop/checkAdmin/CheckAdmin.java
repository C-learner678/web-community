package com.jlu.webcommunity.aop.checkAdmin;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckAdmin {

}
