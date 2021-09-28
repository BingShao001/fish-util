package com.yb.fish.chain.link;


public class AllHandler extends BaseHandler {
    @Override
    protected boolean condition() {
        return true;
    }

    @Override
    protected void handle() {
        System.out.println("all handle do");
    }
}
