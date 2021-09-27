package com.yb.fish.chain.link;

import com.yb.fish.chain.list.BingHandler;
import com.yb.fish.chain.list.HandlerChain;
import com.yb.fish.chain.list.TiaoHandler;
import com.yb.fish.chain.list.YueHandler;

public class Main {

    public static void main(String[] args) {
        BingHandler bingHandler = new BingHandler();
        YueHandler yueHandler = new YueHandler();
        TiaoHandler tiaoHandler = new TiaoHandler();
        HandlerChain handlerChain = new HandlerChain();
        handlerChain.addHandler(bingHandler);
        handlerChain.addHandler(yueHandler);
        handlerChain.addHandler(tiaoHandler);
        handlerChain.handle();

    }
}
