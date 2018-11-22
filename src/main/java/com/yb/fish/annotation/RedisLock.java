package com.yb.fish.annotation;

public @interface RedisLock {
    String bussinessKey() default "redisLock";

    /**
     * tryLock time
     **/
    int waitTime();

    /**
     * 释放锁资源，避免死锁
     **/
    int leaseTime();
}
