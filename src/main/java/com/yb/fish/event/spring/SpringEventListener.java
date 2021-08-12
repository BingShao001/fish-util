package com.yb.fish.event.spring;

import org.springframework.context.ApplicationListener;
/**
* SpringEventListener被自定义监听器继承
* @author bing
* @create 12/08/2021
* @version 1.0
**/
public abstract class SpringEventListener implements ApplicationListener<SpringDomainEvent> {

    @Override
    public void onApplicationEvent(SpringDomainEvent event) {
        execute(event);
    }
    abstract void execute(SpringDomainEvent event);
}
