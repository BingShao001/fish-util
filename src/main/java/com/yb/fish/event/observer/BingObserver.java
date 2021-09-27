package com.yb.fish.event.observer;

public class BingObserver implements Observer {
    @Override
    public void watch() {
        System.out.println("I'm bing watch");
    }
}
