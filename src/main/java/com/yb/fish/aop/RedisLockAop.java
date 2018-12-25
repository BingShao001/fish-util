package com.yb.fish.aop;

import com.alibaba.fastjson.JSON;
import com.yb.fish.annotation.LockKey;
import com.yb.fish.annotation.RedisLock;
import com.yb.fish.constant.FishContants;
import com.yb.fish.exception.OriginalAssert;
import com.yb.fish.lock.InterfaceRedisLock;
import com.yb.fish.utils.ReflectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
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
                OriginalAssert.isRealTrueThrows(null == ret, FishContants.REQ_ERROR, "该接口请求超时");
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
                if (ReflectUtils.isBasicType(arg)) {
                    return bussinessKey + String.valueOf(arg);
                } else if (arg instanceof String) {
                    return bussinessKey + arg;
                } else {
                    Class clazz = arg.getClass();
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields){
                        if (field.isAnnotationPresent(LockKey.class)){
                            field.setAccessible(true);
                            if (ReflectUtils.isBasicType(field.getType().getName())){
                                Object fieldValue = ReflectUtils.getValueByFieldName(field.getName(),arg);
                                return bussinessKey + String.valueOf(fieldValue);
                            }else {
                                Object fieldValue = ReflectUtils.getValueByFieldName(field.getName(),arg);
                                return bussinessKey + JSON.toJSONString(fieldValue);
                            }
                        }else {
                            new RuntimeException("对象属性需要@LockKey修饰");
                        }
                    }

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
