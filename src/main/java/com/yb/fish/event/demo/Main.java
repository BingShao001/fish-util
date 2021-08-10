package com.yb.fish.event.demo;

import com.yb.fish.event.origin.DomainEventPublisher;

public class Main {
    public static void main(String[] args){
        DomainEventPublisher.subscribe(OrderStatusDomainEvent.class,new OrderStatusDomainEventSubscriber());
        DomainEventPublisher.publish(new OrderStatusDomainEvent());
    }
}
