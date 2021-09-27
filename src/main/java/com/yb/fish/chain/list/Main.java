package com.yb.fish.chain.list;

public class Main {

    public static void main(String[] args) {
        BingHandler bingHandler = new BingHandler();
        YueHandler yueHandler = new YueHandler();
        TiaoHandler tiaoHandler = new TiaoHandler();
        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(tiaoHandler);
        handlerChain.addHandler(yueHandler);
        handlerChain.addHandler(bingHandler);
        handlerChain.handle();

    }
}
