package com.yb.fish.event.shared.demo;

import com.yb.fish.event.shared.DomainEvent;

import java.util.Date;
public class OrderStatusDomainEvent implements DomainEvent {
    private String name;
    private String content;

    public OrderStatusDomainEvent() {
    }
    public OrderStatusDomainEvent(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public OrderStatusDomainEvent setName(String name) {
        this.name = name;
        return this;
    }

    public String getContent() {
        return content;
    }

    public OrderStatusDomainEvent setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public Date occurredTime() {
        return new Date();
    }
}
