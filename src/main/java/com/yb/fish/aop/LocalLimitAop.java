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

    private final static String EXECUTION = "execution(* com.*..*.*(..))";

    Logger logger = org.slf4j.LoggerFactory.getLogger(LocalLimitAop.class);

    @Around("pointCutMethod()")
    public Object aroundLog(ProceedingJoinPoint jPoint) throws Throwable {

        Class targeClazz = jPoint.getTarget().getClass();
        String currentMethod = jPoint.getSignature().getName();
        for (Method method : targeClazz.getDeclaredMethods()) {
            if (currentMethod.equals(method.getName())) {
                return this.executeLimit(jPoint, targeClazz, method);
            }
        }
        return null;
    }

    private Object executeLimit(ProceedingJoinPoint jPoint, Class targeClazz, Method method) {

        boolean hasServiceLocalLimit = targeClazz.isAnnotationPresent(LocalLimit.class);
        boolean hasMethodLocalLimit = method.isAnnotationPresent(LocalLimit.class);
        if (!hasServiceLocalLimit && !hasMethodLocalLimit) {
            try {
                return jPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        String className = targeClazz.getName();
        String methodName = method.getName();
        String containerKey = hasServiceLocalLimit && !hasMethodLocalLimit ? className : className + methodName;
        RateLimiter limiter = LocalLimitConfigContainer.getRateLimiter(containerKey);
        Semaphore semaphore = LocalLimitConfigContainer.getSemaphore(containerKey);
        limiter.acquire();
        try {
            semaphore.acquire();
            return jPoint.proceed();
        } catch (Throwable throwable) {
            logger.info("local_limit_error:class_name:{},ex:{}", className, throwable.getMessage());
            semaphore.release();
            OriginalAssert.isRealTrueThrows(true, FishContants.REQ_ERROR, "该接口请求超时");
        } finally {
            semaphore.release();
        }
        return null;
    }

    /**
     * 切入点可以添加其他路径
     */
    @Pointcut(EXECUTION)
    private void pointCutMethod() {
    }


}
