package com.yb.fish.event.guava.demo;

import com.yb.fish.event.guava.DomainEvent;

public class UserEvent extends DomainEvent {
    private String name;
    private int age;
    private UserEventType type = UserEventType.REGISTER;

    public UserEvent() {
    }

    public UserEvent(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public UserEventType getType() {
        return type;
    }

    @Override
    protected String identify() {
        return "unique_event_id";
    }
}
