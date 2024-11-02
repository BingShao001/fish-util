package com.yb.fish.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    long expireTime() default 10; // 幂等记录过期时间，默认10秒
    String requestId() default "requestId"; //可以自定义id，多个功能统一幂等或限制访问使用
}

