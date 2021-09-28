package com.yb.fish.chain.link;


public class HandlerChain {
    private BaseHandler header;
    private BaseHandler tail = null;
    public void addHandler(BaseHandler baseHandler){
        if (null == header){
            header = baseHandler;
            header.setNextHandler(null);
            tail = baseHandler;
            return;
        }
        tail.setNextHandler(baseHandler);
        tail = baseHandler;
    }

    public void handle(){
        header.doHandle();
    }

}
