package com.yb.fish.event.shared;
/**
* DomainEventSubscriber订阅器
* @author bing
* @create 10/08/2021
* @version 1.0
**/
public interface DomainEventSubscriber<T extends DomainEvent> {

    /**
     * 订阅者处理事件
     *
     * @param event 领域事件
     */
    void handle(T event);
}
