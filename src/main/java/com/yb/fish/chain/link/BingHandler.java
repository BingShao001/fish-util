package com.yb.fish.chain.link;

public class BingHandler extends BaseHandler {
    @Override
    protected boolean condition() {
        return true;
    }

    @Override
    protected void handle() {
        System.out.println("bing handle do");
    }
}
