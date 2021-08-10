package com.yb.fish.event.demo;

import com.yb.fish.event.origin.DomainEvent;

import java.util.Date;

public class OrderStatusDomainEvent implements DomainEvent {
    @Override
    public Date occurredTime() {
        return new Date();
    }
    public String orderStatusChange(){
        return "finish";
    }
}
