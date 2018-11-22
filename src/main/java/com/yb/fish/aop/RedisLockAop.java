package com.yb.fish.aop;

import com.yb.fish.annotation.RedisLock;
import com.yb.fish.constant.FishContants;
import com.yb.fish.lock.InterfaceRedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁aop
 * redmin:外部依赖需要scan当前目录
 *
 * @author bing
 * @version 1.0
 * @create 2018/11/12
 **/
@Component
@Aspect
public class RedisLockAop {
    Logger logger = org.slf4j.LoggerFactory.getLogger(RedisLockAop.class);
    private final static String EXECUTION = "@annotation(com.yb.fish.annotation.RedisLock)";
    @Autowired
    InterfaceRedisLock interfaceRedisLock;

    /**
     * 切入点可以添加其他路径
     */
    @Pointcut(EXECUTION)
    private void pointCutMethod() {
    }

    @Around("pointCutMethod()")
    public Object aroundLog(ProceedingJoinPoint jPoint) throws Throwable {
        Class targeClazz = jPoint.getTarget().getClass();
        String targetClassName = targeClazz.getName();
        String currentMethod = jPoint.getSignature().getName();
        String bussinessKey = null;
        Object ret = null;
        for (Method method : targeClazz.getDeclaredMethods()) {
            boolean isRedisLock = currentMethod.equals(method.getName()) && method.isAnnotationPresent(RedisLock.class);
            if (isRedisLock) {
                try {
                    RedisLock redisLock = method.getAnnotation(RedisLock.class);
                    bussinessKey = redisLock.bussinessKey();
                    ret = this.executeLock(redisLock, bussinessKey, jPoint);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("redis_lock_error:class_name:{},ex:{}", targetClassName, e.getMessage());
                } finally {
                    interfaceRedisLock.unLock(bussinessKey);
                }
                return ret;
            }
        }
        return ret;
    }

    /**
     * 根据注解值去选择锁的类型
     *
     * @param redisLock
     * @param bussinessKey
     * @param jPoint
     * @return
     * @throws Throwable
     */
    private Object executeLock(RedisLock redisLock, String bussinessKey, ProceedingJoinPoint jPoint) throws Throwable {
        int waitTime = redisLock.waitTime();
        int leaseTime = redisLock.leaseTime();
        if (leaseTime == FishContants.ZERO) {
            interfaceRedisLock.lock(bussinessKey);
            return jPoint.proceed();
        } else if (waitTime != FishContants.ZERO && leaseTime != FishContants.ZERO) {
            boolean getLock = interfaceRedisLock.lock(bussinessKey, waitTime, leaseTime);
            return getLock ? jPoint.proceed() : null;
        } else if (waitTime == FishContants.ZERO && leaseTime != FishContants.ZERO) {
            boolean getLock = interfaceRedisLock.lock(bussinessKey, waitTime);
            return getLock ? jPoint.proceed() : null;
        }
        return null;
    }

}
