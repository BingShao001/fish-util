package com.yb.fish.builder;

/**
 * @author bing.zhang
 * @title: BingBuilder
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/25下午5:39
 */
public class BingBuilder implements BaseBuilder{
    @Override
    public void build() {
        System.out.println("bing build ...");
    }

    @Override
    public void fill() {
        System.out.println("bing fill ...");
    }
}
