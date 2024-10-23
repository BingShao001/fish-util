package com.yb.fish.aop;
import com.yb.fish.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Aspect
@Component
public class IdempotenceAspect {

    @Autowired
    private IdempotenceComponent idempotenceComponent;

    @Around("@annotation(com.yb.fish.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        long expireTime = idempotent.expireTime();

        // 生成 requestId，可以基于方法名和参数生成
        String requestId = generateRequestId(joinPoint);

        // 幂等性校验
        boolean canProceed = idempotenceComponent.tryProcess(requestId, expireTime);
        if (!canProceed) {
            return "Request has already been processed";
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
        Object[] args = joinPoint.getArgs();
        String argsString = Arrays.toString(args);

        // 将类名、方法名和参数拼接作为唯一标识
        String key = className + ":" + methodName + ":" + argsString;
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }
}