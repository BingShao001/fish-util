package com.yb.fish.event.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringPublisherBean implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void publishEvent(SpringDomainEvent springDomainEvent){
        applicationContext.publishEvent(springDomainEvent);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
