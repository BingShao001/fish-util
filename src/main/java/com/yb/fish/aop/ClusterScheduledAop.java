package com.yb.fish.aop;


import com.yb.fish.annotation.ClusterScheduled;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
* OffsetAop
* @author bing
* @create 19/11/2021
* @version 1.0
**/
@Component
@Aspect
public class ClusterScheduledAop {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    Logger logger = org.slf4j.LoggerFactory.getLogger(ClusterScheduledAop.class);

    private final static String EXECUTION = "execution(* com.yb.*..*.*(..)) && @annotation(com.yb.fish.annotation.ClusterScheduled)";

    /**
     * 切入点可以添加其他路径
     */
    @Pointcut(EXECUTION)
    private void pointCutMethod() {
    }

    /***
     * @param jPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCutMethod()")
    public void aroundLog(ProceedingJoinPoint jPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) jPoint.getSignature();
        Method method = methodSignature.getMethod();
        boolean isClusterScheduled = method.isAnnotationPresent(ClusterScheduled.class);
        if (!isClusterScheduled) {
            jPoint.proceed();
            return;
        }
        ClusterScheduled clusterScheduled = method.getAnnotation(ClusterScheduled.class);
        String lockKey = clusterScheduled.lockKey();
        if (isDuplicate(lockKey)) {
            return;
        }
        jPoint.proceed();
        storeKey(lockKey);
        return;
    }

    private boolean isDuplicate(String lockKey) {
        String roomUserExpiredFlag = redisTemplate.opsForValue().get(lockKey);
        return StringUtils.isNotBlank(roomUserExpiredFlag);
    }

    private void storeKey(String lockKey) {
        redisTemplate.opsForValue().set(lockKey, "1", 2, TimeUnit.SECONDS);
    }
}
