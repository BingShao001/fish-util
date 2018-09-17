package com.yb.fish.aop;

import com.yb.fish.annotation.Offset;
import com.yb.fish.delay.InterfaceOffsetNetworkDelayQueue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 补偿机制aop
 * redmin:外部依赖需要scan当前目录
 *
 * @author bing
 * @version 1.0
 * @create 2018/9/12
 **/
@Component
@Aspect
public class OffsetAop {
    @Autowired
    private InterfaceOffsetNetworkDelayQueue offsetNetworkDelayQueue;
    Logger logger = org.slf4j.LoggerFactory.getLogger(OffsetAop.class);
    private final static String EXECUTION = "execution(* com.yb.common.auth.service..*.*(..))";

    /**
     * 切入点可以添加其他路径
     */
    @Pointcut(EXECUTION)
    private void pointCutMethod() {
    }

    /***
     * 切面处理日志功能
     * @param jPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCutMethod()")
    public Object aroundLog(ProceedingJoinPoint jPoint) throws Throwable {
        Class targeClazz = jPoint.getTarget().getClass();
        String targetClassName = targeClazz.getName();
        String currentMethod = jPoint.getSignature().getName();
        for (Method method : targeClazz.getDeclaredMethods()) {
            boolean isOffset = currentMethod.equals(method.getName()) && method.isAnnotationPresent(Offset.class);
            if (isOffset) {
                Object ret = null;
                try {
                    ret = jPoint.proceed();
                } catch (Exception e) {
                    //执行补偿处理
                    logger.warn("offset : service:{},method:{}", targetClassName, currentMethod);
                    offsetNetworkDelayQueue.offsetTask(jPoint);
                }
                return ret;
            }
        }
        return jPoint.proceed();
    }
}
