package com.yb.fish.init;

import com.google.common.util.concurrent.RateLimiter;
import com.yb.fish.annotation.LocalLimit;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

/**
 * @author bing.zhang
 * @title: LocalLimitConfig
 * @projectName common-yb-fish-utils
 * @description: LocalLimitConfig
 * @date 2019/12/31下午5:06
 */
@Component
public class LocalLimitConfig implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] beansName = applicationContext.getBeanDefinitionNames();
        for (String beanName : beansName) {
            Class<?> beanType = applicationContext.getType(beanName);
            boolean hasLocalLimit = beanType.isAnnotationPresent(LocalLimit.class);
            if (hasLocalLimit) {
                this.serviceLocalLimit(beanType);
            } else {
                this.methodLocalLimit(beanType);
            }
        }
    }

    private void serviceLocalLimit(Class<?> beanType){
        String className = beanType.getName();
        LocalLimit localLimit = beanType.getAnnotation(LocalLimit.class);
        RateLimiter rateLimiter = RateLimiter.create(localLimit.qps());
        LocalLimitConfigContainer.setRateLimiter(className, rateLimiter);
        Semaphore semaphore = new Semaphore(localLimit.qps());
        LocalLimitConfigContainer.setSemaphore(className, semaphore);
    }

    private void methodLocalLimit(Class<?> beanType){
        Method[] methods = beanType.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(LocalLimit.class)){
                String className = beanType.getName();
                String methodName = method.getName();
                LocalLimit localLimit =  method.getAnnotation(LocalLimit.class);
                RateLimiter rateLimiter = RateLimiter.create(localLimit.qps());
                LocalLimitConfigContainer.setRateLimiter(className+methodName, rateLimiter);
                Semaphore semaphore = new Semaphore(localLimit.qps());
                LocalLimitConfigContainer.setSemaphore(className+methodName, semaphore);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
