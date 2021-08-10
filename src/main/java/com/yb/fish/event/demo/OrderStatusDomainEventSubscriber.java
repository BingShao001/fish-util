package com.yb.fish.event.demo;

import com.yb.fish.event.origin.DomainEventSubscriber;

public class OrderStatusDomainEventSubscriber implements DomainEventSubscriber<OrderStatusDomainEvent> {
    @Override
    public void handle(OrderStatusDomainEvent event) {
        System.out.println("事件发生：" + event.occurredTime() + "order status " + event.orderStatusChange());
    }
}
