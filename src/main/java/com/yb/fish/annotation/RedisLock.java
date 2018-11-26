package com.yb.fish.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLock {
    String bussinessKey() default "redisLock";

    /**
     * tryLock time
     **/
    int waitTime() default 0;

    /**
     * 释放锁资源，避免死锁
     **/
    int leaseTime() default 0;
}
