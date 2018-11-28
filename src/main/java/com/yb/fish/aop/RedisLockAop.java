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
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

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
        String realLockKey = null;
        Object ret = null;
        for (Method method : targeClazz.getDeclaredMethods()) {
            boolean isRedisLock = currentMethod.equals(method.getName()) && method.isAnnotationPresent(RedisLock.class);
            if (isRedisLock) {
                try {
                    RedisLock redisLock = method.getAnnotation(RedisLock.class);
                    String bussinessKey = redisLock.bussinessKey();
                    Map<Parameter, Object> typeValueParam = this.setMethodMap(method, jPoint);
                    realLockKey = this.getRelKey(bussinessKey, typeValueParam);
                    ret = this.executeLock(redisLock, realLockKey, jPoint);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("redis_lock_error:class_name:{},ex:{}", targetClassName, e.getMessage());
                } finally {
                    interfaceRedisLock.unLock(realLockKey);
                }
                return ret;
            }
        }
        return ret;
    }

    private Map<Parameter, Object> setMethodMap(Method method, ProceedingJoinPoint jPoint) {
        Map<Parameter, Object> typeValueParam = new HashMap(FishContants.FOUR);
        Parameter[] parameters = method.getParameters();
        Object[] args = jPoint.getArgs();
        for (int idx = FishContants.ZERO; idx < parameters.length; idx++) {
            typeValueParam.put(parameters[idx], args[idx]);
        }
        return typeValueParam;
    }

    /**
     * 获取目标方法的参数key锁
     *
     * @param bussinessKey
     * @param typeValueParam
     * @return
     */
    private String getRelKey(String bussinessKey, Map<Parameter, Object> typeValueParam) {
        for (Map.Entry<Parameter, Object> typeAndValue : typeValueParam.entrySet()) {
            Parameter parameter = typeAndValue.getKey();
            Object arg = typeAndValue.getValue();
            if (parameter.isAnnotationPresent(LockKey.class)) {
                if (arg instanceof java.lang.Integer ||
                        arg instanceof java.lang.Byte ||
                        arg instanceof java.lang.Long ||
                        arg instanceof java.lang.Double ||
                        arg instanceof java.lang.Float ||
                        arg instanceof java.lang.Character ||
                        arg instanceof java.lang.Short ||
                        arg instanceof java.lang.Boolean) {
                    return bussinessKey + String.valueOf(arg);
                } else if (arg instanceof String) {
                    return bussinessKey + arg;
                } else {
                    new RuntimeException("指定的参数key只能为：8大基础类型和String类型");
                }
            }
        }
        return bussinessKey;
    }

    /**
     * 根据注解值去选择锁的类型
     *
     * @param redisLock
     * @param realLockKey
     * @param jPoint
     * @return
     * @throws Throwable
     */
    private Object executeLock(RedisLock redisLock, String realLockKey, ProceedingJoinPoint jPoint) throws Throwable {
        System.out.println("realLockKey : " + realLockKey);
        int waitTime = redisLock.waitTime();
        int leaseTime = redisLock.leaseTime();
        if (leaseTime == FishContants.ZERO && waitTime == FishContants.ZERO) {
            interfaceRedisLock.lock(realLockKey);
            return jPoint.proceed();
        } else if (waitTime != FishContants.ZERO && leaseTime != FishContants.ZERO) {
            boolean getLock = interfaceRedisLock.lock(realLockKey, waitTime, leaseTime);
            return getLock ? jPoint.proceed() : null;
        } else if (waitTime == FishContants.ZERO && leaseTime != FishContants.ZERO) {
            boolean getLock = interfaceRedisLock.lock(realLockKey, waitTime);
            return getLock ? jPoint.proceed() : null;
        }
        return null;
    }


}
