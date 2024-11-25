package io.jagat.config.limit;

import io.jagat.config.context.AppRequestContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableAspectJAutoProxy
public class RateLimitConfiguration {

    @Bean
    @DependsOn("redisTemplate")
    public RateLimitAspect rateLimitAspect(AppRequestContext appRequestContext, RedisTemplate redisTemplate) {
        return new RateLimitAspect(appRequestContext,redisTemplate);
    }
}
