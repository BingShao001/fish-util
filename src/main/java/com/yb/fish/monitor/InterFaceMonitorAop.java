package com.yb.fish.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class InterFaceMonitorAop {
    public final static long DEFAULT_COST = 2000;
    Logger logger = LoggerFactory.getLogger(InterFaceMonitorAop.class);

    @Pointcut("execution(* com.yb.datasync..*Controller1.*(..))")
    private void controllerMethods() {
    }

    @Pointcut("@annotation(com.yb.datasync.aop.MethodMonitor)")
    private void annotatedMethods() {
    }


    @Around("controllerMethods() || annotatedMethods()")
    public void aroundLog(ProceedingJoinPoint jPoint) throws Throwable {
        Class<?> targetClass = jPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) jPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        // 获取方法参数值
        Object[] args = jPoint.getArgs();
        try {
            long start = System.currentTimeMillis();
            jPoint.proceed();
            long finish = System.currentTimeMillis();
            long realCost = finish - start;

            if (realCost >= this.getCost(method)) {
                logger.error("cost_log method:{} params:{} cost:{}ms", getMethodFullName(targetClass, method), args, realCost);
            }
        } catch (Throwable throwable) {
            logger.error("error_interceptor method:{} params:{}  Error:{} ", getMethodFullName(targetClass, method), args, throwable);
            throw throwable;
        }
    }

    /**
     * 1.优先取配在方法上的耗时阈值；
     * 2.如果都没有配置就获取默认值
     *
     * @param method
     * @return
     */
    private long getCost(Method method) {
        MethodMonitor methodMonitor = method.getAnnotation(MethodMonitor.class);
        return null != methodMonitor ? methodMonitor.value() : DEFAULT_COST;
    }

    private String getMethodFullName(Class<?> targetClass, Method method) {
        // 获取方法名
        String methodName = method.getName();
        String targetClassName = targetClass.getName();

        // 获取参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuilder parameterTypesString = new StringBuilder();
        for (Class<?> paramType : parameterTypes) {
            if (parameterTypesString.length() > 0) {
                parameterTypesString.append(", ");
            }
            parameterTypesString.append(paramType.getName());
        }
        String methodFullName = targetClassName + "." + methodName + "(" + parameterTypesString.toString() + ")";
        return methodFullName;
    }
}
