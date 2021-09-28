package com.yb.fish.chain.link;



public class Main {

    public static void main(String[] args) {
        BingHandler bingHandler = new BingHandler();
        YueHandler yueHandler = new YueHandler();
        TiaoHandler tiaoHandler = new TiaoHandler();
        AllHandler allHandler = new AllHandler();
        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(bingHandler);
        handlerChain.addHandler(yueHandler);
        handlerChain.addHandler(tiaoHandler);
        handlerChain.addHandler(allHandler);
        handlerChain.handle();

    }
}
