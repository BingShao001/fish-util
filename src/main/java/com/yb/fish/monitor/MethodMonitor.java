package com.yb.fish.monitor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
* InterfaceMonitor 接口耗时拦截注解
* @author bing
* @create 2024/7/30
* @version 1.0
**/

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodMonitor {


    /**
     * 单位是ms
     * @return
     */
    long value() default 1000;
}
