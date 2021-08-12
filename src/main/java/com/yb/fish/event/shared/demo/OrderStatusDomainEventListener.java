package com.yb.fish.event.shared.demo;

import com.yb.fish.event.shared.DomainEventSubscriber;

public class OrderStatusDomainEventListener implements DomainEventSubscriber<OrderStatusDomainEvent> {
    @Override
    public void handle(OrderStatusDomainEvent event) {
        System.out.println("订单变化事件发生：" + event.occurredTime());
        // TODO
        // 监听领域事件后做一些业务处理
    }
}
