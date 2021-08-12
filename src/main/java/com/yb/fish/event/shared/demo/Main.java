package com.yb.fish.event.shared.demo;

import com.yb.fish.event.shared.DomainEventPublisher;

public class Main {
    public static void main(String[] args){
        DomainEventPublisher.subscribe(OrderStatusDomainEvent.class,new OrderStatusDomainEventListener());
        for (int i = 0; i < 5; i++) {
            DomainEventPublisher.publish(new OrderStatusDomainEvent());
        }
    }
}
