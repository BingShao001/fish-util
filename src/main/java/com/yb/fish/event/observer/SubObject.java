package com.yb.fish.event.observer;

import java.util.ArrayList;
import java.util.List;

public class SubObject {
    private List<Observer> observerList = new ArrayList<>();
    public void register(Observer observer){
        observerList.add(observer);
    }
    public void notifyObserver(){
        for (Observer observer : observerList) {
            observer.watch();
        }
    }
}
