package com.yb.fish.job.config;

import com.yb.fish.job.delay.DelayShardComponent;
import com.yb.fish.job.delay.RedisDelayComponent;
import com.yb.fish.job.normal.ClusterScheduledAop;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Aspect
@EnableScheduling
public class JobConfiguration {

    // redisTemplate依赖自动装配
    @Bean
    public RedisDelayComponent redisDelayComponent(StringRedisTemplate redisTemplate){
        RedisDelayComponent redisDelayComponent = new RedisDelayComponent(redisTemplate,new DelayShardComponent(""));
        return redisDelayComponent;
    }
    @Bean
    public ClusterScheduledAop clusterScheduledAop(RedisTemplate redisTemplate){
        ClusterScheduledAop clusterScheduledAop = new ClusterScheduledAop(redisTemplate);
        return clusterScheduledAop;
    }
}
