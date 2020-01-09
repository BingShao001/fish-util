package com.yb.fish.config;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bing.zhang
 * @title: FishServerDefinitionParser
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2020/1/7下午5:26
 */
public class FishServerDefinitionParser implements BeanDefinitionParser {
    private Class<?> clazz;
    private static final String default_prefix = "fish-";
    private static final AtomicLong COUNT = new AtomicLong(0);

    public FishServerDefinitionParser(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        return parseHelper(element, parserContext, this.clazz);
    }
    private BeanDefinition parseHelper(Element element, ParserContext parserContext, Class<?> clazz) {
        RootBeanDefinition bd = new RootBeanDefinition();

        bd.setLazyInit(false);
        String id = element.getAttribute("id");
        if (id == null || id.isEmpty()) {
            id = default_prefix + COUNT.getAndDecrement();
        }

        String serverName = element.getAttribute("serverName");

        bd.setBeanClass(clazz);
        bd.setInitMethodName("init");

        MutablePropertyValues propertyValues = bd.getPropertyValues();
        propertyValues.addPropertyValue("serverName", serverName);
        propertyValues.addPropertyValue("id",id);
        parserContext.getRegistry().registerBeanDefinition(id, bd);

        return bd;
    }
}
