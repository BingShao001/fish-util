package com.yb.fish.chain.link;

import com.yb.fish.chain.list.BaseHandler;

public class YueHandler extends BaseHandler {
    @Override
    protected boolean condition() {
        return true;
    }

    @Override
    protected void handle() {
        System.out.println("yue handle do");
    }
}
