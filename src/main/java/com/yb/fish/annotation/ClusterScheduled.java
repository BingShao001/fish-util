package com.yb.fish.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
*  用来做集群环境定时任务去重，时间2s，注：不要用来做2s以内的定时任务执行
* @author bing
* @create 19/11/2021
* @version 1.0
**/
@Target( {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scheduled
public @interface ClusterScheduled {
    /**
     * 其他需要自己去扩展
     **/
    @AliasFor(annotation = Scheduled.class, attribute = "cron")
    String cron() default "";

    String lockKey() default "lock_key";
}
