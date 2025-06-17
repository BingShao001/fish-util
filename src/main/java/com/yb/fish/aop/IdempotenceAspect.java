package com.yb.fish.aop;

import com.alibaba.fastjson.JSON;
import com.yb.fish.annotation.Idempotent;
import com.yb.fish.annotation.IdempotentParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Aspect
public class IdempotenceAspect {
    Logger logger = LoggerFactory.getLogger(IdempotenceAspect.class);

    private final IdempotenceComponent idempotenceComponent;

    public IdempotenceAspect(IdempotenceComponent idempotenceComponent) {
        this.idempotenceComponent = idempotenceComponent;
    }

    @Around("@annotation(com.yb.fish.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        long expireTime = idempotent.expireTime();

        // 生成 requestId，可以基于方法名和参数生成
        String requestId = "requestId".equals(idempotent.requestId()) ? generateRequestId(joinPoint) : idempotent.requestId();

        // 幂等性校验
        boolean canProceed = idempotenceComponent.tryProcess(requestId, expireTime);
        if (!canProceed) {
            String methodName = signature.getName();
            String className = joinPoint.getTarget().getClass().getName();
            logger.error("Request has already been processed. class: {} method: {} expireTime: {}", className, methodName, expireTime);
            throw new RuntimeException("The current request is restricted.");
        }

        Object result;
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Exception e) {
            // 如果执行失败，删除幂等记录
            idempotenceComponent.remove(requestId);
            throw e;
        }

        return result;
    }

    private String generateRequestId(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getName();  // 获取类名
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = joinPoint.getArgs();
        StringBuffer  argsStringBuffer = new StringBuffer();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof IdempotentParam) {
                    Object annotatedArg = args[i];
                    argsStringBuffer.append(JSON.toJSONString(annotatedArg));
                    argsStringBuffer.append(":");
                }
            }
        }
        // 将类名、方法名和参数拼接作为唯一标识
        String key = className + ":" + methodName + ":" + argsStringBuffer.toString();
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }
}