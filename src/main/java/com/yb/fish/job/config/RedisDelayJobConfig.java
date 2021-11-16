package com.yb.fish.job.config;


import com.yb.fish.job.RedisDelayTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

//@Configuration
public class RedisDelayJobConfig {

    @Bean
    @ConditionalOnMissingBean(RedisDelayTask.class)
    public RedisDelayTask redisDelayTask(RedisTemplate redisTemplate) {
        return new RedisDelayTask(redisTemplate);
    }
}
