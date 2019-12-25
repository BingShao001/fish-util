package com.yb.fish.builder;

/**
 * @author bing.zhang
 * @title: BuilderExecuter
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/25下午5:36
 */
public class BuilderExecuter<T extends BaseBuilder> {
    private T t;

    public void execute(){
        t.build();
        t.fill();
    }

    public T getT() {
        return t;
    }

    public BuilderExecuter<T> setT(T t) {
        this.t = t;
        return this;
    }
}
