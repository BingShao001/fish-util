package com.yb.fish.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author bing.zhang
 * @title: FishNameSpace
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2020/1/7下午5:23
 */
public class FishNameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        this.registerBeanDefinitionParser("service",
                new FishServerDefinitionParser(ServerBean.class));
    }
}
