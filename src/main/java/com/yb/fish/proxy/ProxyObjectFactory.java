package com.yb.fish.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author bing.zhang
 * @title: ProxyObjectFactory
 * @projectName common-yb-fish-utils
 * @description: 代理对象工厂
 * @date 2019/12/1下午8:32
 */
public class ProxyObjectFactory {

    public static <T> T getCglibProxyObject(Class<T> targetClazz, MethodInterceptor methodInterceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClazz);
        enhancer.setCallback(methodInterceptor);
        return (T) enhancer.create();
    }

    public static <T> T getJdkProxyObject(Class<T> targetClazz, InvocationHandler h) {
        return (T) Proxy.newProxyInstance(targetClazz.getClassLoader(), new Class[]{targetClazz}, h);
    }
}
