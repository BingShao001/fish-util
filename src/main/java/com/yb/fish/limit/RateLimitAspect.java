package com.yb.fish.limit;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Collections;

@Aspect
public class RateLimitAspect {

    private final AppRequestContext appRequestContext;
    private final RedisTemplate<String, String> redisTemplate;

    public RateLimitAspect(AppRequestContext appRequestContext, RedisTemplate redisTemplate) {
        this.appRequestContext = appRequestContext;
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = generateKey(joinPoint, rateLimit);

        if (isLimited(rateLimit, key)) {
            throw new RuntimeException("key : " + key + " Rate limit exceeded");
        }

        return joinPoint.proceed();
    }

    /**
     * 生成限流键
     */
    private String generateKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        JwtUser jwtUser = appRequestContext.get().getJwtUser();
        // 默认使用类名+方法名作为限流键
        String methodName = joinPoint.getSignature().toShortString();
        String preKey = StringUtils.isBlank(rateLimit.preKey()) ? "rate_limit:" : rateLimit.preKey();

        if (null == jwtUser) {
            return preKey;
        }


        return preKey + methodName + jwtUser.getUserId();
    }

    private boolean isLimited(RateLimit rateLimit, String key) {
        String luaScript = "local key = KEYS[1] " +
                "local limit = tonumber(ARGV[1]) " +
                "local window = tonumber(ARGV[2]) " +
                "local now = tonumber(ARGV[3]) " +
                "redis.call('ZREMRANGEBYSCORE', key, 0, now - window) " +
                "local count = redis.call('ZCARD', key) " +
                "if count < limit then " +
                "   redis.call('ZADD', key, now, now) " +
                "   redis.call('EXPIRE', key, math.ceil(window / 1000)) " +
                "   return 1 " +
                "else " +
                "   return 0 " +
                "end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);


        Long result = redisTemplate.execute(redisScript,
                Collections.singletonList(key),
                Arrays.asList(String.valueOf(rateLimit.limit()),
                        String.valueOf(rateLimit.timeWindow()),
                        String.valueOf(System.currentTimeMillis())));
        return Integer.parseInt(result.toString()) == 1;

    }
}
