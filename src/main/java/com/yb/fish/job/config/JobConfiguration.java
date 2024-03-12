package com.yb.fish.job.config;

import com.yb.fish.job.delay.RedisDelayComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class JobConfiguration {

    @Bean
    public RedisDelayComponent redisDelayComponent(RedisTemplate<String,String> redisTemplate){
        RedisDelayComponent redisDelayComponent = new RedisDelayComponent(redisTemplate);
        return redisDelayComponent;
    }
}
