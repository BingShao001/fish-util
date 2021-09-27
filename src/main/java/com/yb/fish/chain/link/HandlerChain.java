package com.yb.fish.chain.link;


public class HandlerChain {
    private BaseHandler header;
    private BaseHandler tail = null;
    public void addHandler(BaseHandler baseHandler){
        if (null == header){
            header = baseHandler;
            header.setNextHandler(tail);
            return;
        }
        header.setNextHandler(baseHandler);
        baseHandler.setNextHandler(tail);
    }

    public void handle(){
        header.doHandle();
    }

}
