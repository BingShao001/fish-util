package com.yb.fish.event.guava.demo;

import com.yb.fish.event.guava.GuavaDomainEventPublisher;

public class UserEventPublisher extends GuavaDomainEventPublisher {
    @Override
    public String identify() {
        return "user_event_publisher";
    }
}
