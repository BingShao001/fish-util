package com.yb.fish.event.observer;

public class Main {
    public static void main(String[] args) {
        Observer bingObserver = new BingObserver();
        Observer yueObserver = new YueObserver();
        SubObject subObject = new SubObject();
        subObject.register(bingObserver);
        subObject.register(yueObserver);
        subObject.notifyObserver();
    }
}
