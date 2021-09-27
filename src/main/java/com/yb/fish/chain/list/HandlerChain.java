package com.yb.fish.chain.list;

import java.util.ArrayList;
import java.util.List;

public class HandlerChain {
    private List<BaseHandler> baseHandlerList = new ArrayList<>();

    public void addHandler(BaseHandler baseHandler){
        baseHandlerList.add(baseHandler);
    }

    public void handle(){
        for (BaseHandler baseHandler : baseHandlerList) {
            baseHandler.doHandle();
        }
    }

}
