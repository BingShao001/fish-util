package com.yb.fish.chain.list;

public class BingHandler extends BaseHandler{
    @Override
    protected boolean condition() {
        return true;
    }

    @Override
    protected void handle() {
        System.out.println("bing handle do");
    }
}
