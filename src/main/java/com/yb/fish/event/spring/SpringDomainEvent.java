package com.yb.fish.event.spring;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * Created by Michael Jiang on 16/1/12.
 */
public abstract class SpringDomainEvent extends ApplicationEvent {
    private Date occurredTime;
    private String name;
    protected abstract String identify();

    public SpringDomainEvent(Object source, String name) {
        super(source);
        this.name = name;
        occurredTime =new Date();
    }

    public Date getOccurredTime() {
        return occurredTime;
    }
}
