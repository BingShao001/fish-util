package com.yb.fish.builder;

/**
 * @author bing.zhang
 * @title: Main
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/25下午5:40
 */
public class Main {
    public static void main(String[] args) {
        new BuilderExecuter<BingBuilder>().setT(new BingBuilder()).execute();
    }
}
