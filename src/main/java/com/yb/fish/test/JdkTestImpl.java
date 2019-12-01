package com.yb.fish.test;

import org.aspectj.weaver.ast.Test;

/**
 * @author bing.zhang
 * @title: TestImpl
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/1下午8:39
 */
public class JdkTestImpl implements JdkTest {
    @Override
    public void say() {
        System.out.println("I'm JdkTestImpl.....");
    }
}
