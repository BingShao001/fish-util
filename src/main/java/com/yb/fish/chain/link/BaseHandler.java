package com.yb.fish.chain.link;

public abstract class BaseHandler {


    private BaseHandler nextHandler;

    public void setNextHandler(BaseHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected abstract boolean condition();

    protected abstract void handle();

    public void doHandle() {
        if (condition()) {
            handle();
        }
        if (null == nextHandler){
            nextHandler.doHandle();
        }
    }
}
