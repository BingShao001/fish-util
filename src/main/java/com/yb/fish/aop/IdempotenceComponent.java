package com.yb.fish.aop;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class IdempotenceComponent {

    private final StringRedisTemplate redisTemplate;

    public IdempotenceComponent(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean tryProcess(String requestId, long expireTime) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(requestId, "1", expireTime, TimeUnit.SECONDS);
        return success != null && success;
    }

    public void remove(String requestId) {
        redisTemplate.delete(requestId);
    }
}
