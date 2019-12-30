package com.yb.fish.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.yb.fish.annotation.LocalLimit;
import com.yb.fish.constant.FishContants;
import com.yb.fish.exception.OriginalAssert;
import com.yb.fish.lock.InterfaceRedisLock;
import com.yb.fish.test.SemaphoreUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

/**
 * 单点限流器aop
 * redmin:外部依赖需要scan当前目录
 *
 * @author bing
 * @version 1.0
 * @create 2018/11/12
 **/
@Component
@Aspect
public class LocalLimitAop {
    Logger logger = org.slf4j.LoggerFactory.getLogger(LocalLimitAop.class);
    private final static String EXECUTION = "@annotation(com.yb.fish.annotation.LocalLimit)";
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

        String currentMethod = jPoint.getSignature().getName();
        Object ret = null;
        for (Method method : targeClazz.getDeclaredMethods()) {
            boolean isLocalLimit = currentMethod.equals(method.getName()) && method.isAnnotationPresent(LocalLimit.class);
            if (isLocalLimit) {
                this.executeLimit(jPoint, method,targeClazz);
                return ret;
            }
        }
        return ret;
    }

    private void executeLimit(ProceedingJoinPoint jPoint,Method method,Class targeClazz){
        LocalLimit localLimit = method.getAnnotation(LocalLimit.class);
        int qps = localLimit.qps();
        RateLimiter limiter = RateLimiter.create(qps);
        Semaphore semaphore = SemaphoreUtils.getSemaphoreInstance(qps);
        limiter.acquire();
        try {
            semaphore.acquire(qps);
            jPoint.proceed();
        } catch (Throwable throwable) {
            String targetClassName = targeClazz.getName();
            logger.info("local_limit_error:class_name:{},ex:{}", targetClassName, throwable.getMessage());
            semaphore.release();
            OriginalAssert.isRealTrueThrows(true, FishContants.REQ_ERROR, "该接口请求超时");
        }finally {
            semaphore.release();
        }
    }



}
