package com.yb.fish.init;

import com.google.common.util.concurrent.RateLimiter;
import com.yb.fish.annotation.LocalLimit;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
                String className = beanType.getName();
                LocalLimit localLimit = beanType.getAnnotation(LocalLimit.class);
                RateLimiter rateLimiter = RateLimiter.create(localLimit.qps());
                LocalLimitConfigContainer.setRateLimiter(className, rateLimiter);
                Semaphore semaphore = new Semaphore(localLimit.qps());
                LocalLimitConfigContainer.setSemaphore(className, semaphore);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
