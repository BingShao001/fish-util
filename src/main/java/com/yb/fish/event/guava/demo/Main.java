package com.yb.fish.event.guava.demo;

public class Main {
    public static void main(String[] args) {
        UserRegisterListener userRegisterListener = new UserRegisterListener();
        UserEventPublisher userEventPublisher = new UserEventPublisher();
        userEventPublisher.register(userRegisterListener);
        UserEvent userEvent = new UserEvent("zhangbing",32);
        for (int i = 0; i < 3; i++) {
            userEventPublisher.asyncPublish(userEvent);
            userEventPublisher.publish(userEvent);
        }
    }
}
