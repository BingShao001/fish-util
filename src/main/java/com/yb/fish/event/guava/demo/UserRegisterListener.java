package com.yb.fish.event.guava.demo;

import com.google.common.eventbus.Subscribe;

public class UserRegisterListener {
    @Subscribe
    public void listenUserRegisterEvent(UserEvent userEvent) {
        if (userEvent.getType().equals(UserEventType.REGISTER)) {
            System.out.println("user register " + userEvent.toString());
            //TODO
            //监听事件处理业务
        }
    }
}
