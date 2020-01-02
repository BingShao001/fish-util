package com.yb.fish.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.yb.fish.annotation.LocalLimit;
import com.yb.fish.constant.FishContants;
import com.yb.fish.exception.OriginalAssert;
import com.yb.fish.init.LocalLimitConfigContainer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
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
    private final static String EXECUTION = "execution(* com.*..*.*(..))";

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
        boolean hasLocalLimit = targeClazz.isAnnotationPresent(LocalLimit.class);
        for (Method method : targeClazz.getDeclaredMethods()) {
            boolean isCurrent = currentMethod.equals(method.getName());
            if (hasLocalLimit && isCurrent) {
                this.executeLimit(jPoint,targeClazz);
                return ret;
            }
        }
        return ret;
    }

    private void executeLimit(ProceedingJoinPoint jPoint, Class targeClazz) {

        String className = targeClazz.getName();
        RateLimiter limiter = LocalLimitConfigContainer.getRateLimiter(className);
        Semaphore semaphore = LocalLimitConfigContainer.getSemaphore(className);
        limiter.acquire();
        try {
            semaphore.acquire();
            jPoint.proceed();
        } catch (Throwable throwable) {
            logger.info("local_limit_error:class_name:{},ex:{}", className, throwable.getMessage());
            semaphore.release();
            OriginalAssert.isRealTrueThrows(true, FishContants.REQ_ERROR, "该接口请求超时");
        } finally {
            semaphore.release();
        }
    }



}
