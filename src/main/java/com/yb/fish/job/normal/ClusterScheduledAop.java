package com.yb.fish.job.normal;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
public class ClusterScheduledAop {

    Logger logger = LoggerFactory.getLogger(ClusterScheduledAop.class);

    private final RedisTemplate<String, String> redisTemplate;
    private final static String EXECUTION = "@annotation(io.jagat.job.normal.ClusterScheduled)";

    public ClusterScheduledAop(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut(EXECUTION)
    private void pointCutMethod() {
    }

    @Around("pointCutMethod()")
    public Object aroundLog(ProceedingJoinPoint jPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) jPoint.getSignature();
        Method method = methodSignature.getMethod();

        if (!method.isAnnotationPresent(ClusterScheduled.class)) {

            return jPoint.proceed();
        }

        ClusterScheduled clusterScheduled = method.getAnnotation(ClusterScheduled.class);
        String lockKeyValue = clusterScheduled.lockKey();
        String taskStatusKey = StringUtils.isBlank(lockKeyValue) ? jPoint.getTarget().getClass().getName() : lockKeyValue;

        if (!redisTemplate.opsForValue().setIfAbsent(taskStatusKey, "RUNNING", 900, TimeUnit.MILLISECONDS)) {
            logger.warn("Task {} is already running, skipping execution.", method.getName());
            return null;
        }
        return jPoint.proceed();

    }
}