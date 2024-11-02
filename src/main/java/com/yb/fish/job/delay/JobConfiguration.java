package com.yb.fish.job.delay;

import com.yb.fish.job.normal.ClusterScheduledAop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAspectJAutoProxy
@EnableScheduling
public class JobConfiguration {

    // redisTemplate依赖自动装配
    @Bean
    public DelayShardComponent delayShardComponent(@Value("${delay_shard_config}") String delayShardConfigValue) {
        return new DelayShardComponent(delayShardConfigValue);
    }

    @Bean
    public RedisDelayComponent redisDelayComponent(StringRedisTemplate redisTemplate, DelayShardComponent delayShardComponent) {
        RedisDelayComponent redisDelayComponent = new RedisDelayComponent(redisTemplate, delayShardComponent);
        return redisDelayComponent;
    }

    @Bean
    public ClusterScheduledAop clusterScheduledAop(RedisTemplate redisTemplate) {
        ClusterScheduledAop clusterScheduledAop = new ClusterScheduledAop(redisTemplate);
        return clusterScheduledAop;
    }


}
