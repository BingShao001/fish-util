package com.yb.fish.chain.link;


public class TiaoHandler extends BaseHandler {
    @Override
    protected boolean condition() {
        return true;
    }

    @Override
    protected void handle() {
        System.out.println("tiao handle do");
    }
}
