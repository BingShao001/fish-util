package com.yb.fish.chain.list;

public abstract class BaseHandler {

    protected abstract boolean condition();

    protected abstract void handle();

    public void doHandle() {
        if (condition()) {
            handle();
        }
    }

}
