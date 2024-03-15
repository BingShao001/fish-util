package com.yb.fish.job.normal;


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
 *
 * @author bing
 * @version 1.0
 * @create 19/11/2021
 **/
@Aspect
public class ClusterScheduledAop {

    private RedisTemplate<String, String> redisTemplate;

    public ClusterScheduledAop(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    Logger logger = org.slf4j.LoggerFactory.getLogger(ClusterScheduledAop.class);

    private final static String EXECUTION = "@annotation(io.utown.job.normal.ClusterScheduled)";

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
        String lockKeyValue = clusterScheduled.lockKey();
        String lockKey = StringUtils.isBlank(lockKeyValue) ? jPoint.getTarget().getClass().getName() : lockKeyValue;
        if (!acquireLock(lockKey)) {
            // 获取锁失败，重复执行，直接返回；
            return;
        }

        jPoint.proceed();
        //暂时先不手动释放，执行时间太短会有并发
//        releaseLock(lockKey);

    }

    private boolean acquireLock(String lockKey) {
        // 尝试获取锁，如果成功则返回true，否则返回false
        return redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 800, TimeUnit.MILLISECONDS);
    }

    private void releaseLock(String lockKey) {
        // 延迟释放锁，确保任务执行完成后的一段时间内不会被其他节点获取到锁
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 删除锁
        redisTemplate.delete(lockKey);
    }
}