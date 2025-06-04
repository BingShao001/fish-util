package com.yb.fish.proxy;

import com.yb.fish.builder.BaseBuilder;
import com.yb.fish.builder.BingBuilder;
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
        return (T) Proxy.newProxyInstance(targetClazz.getClassLoader(), targetClazz.getInterfaces(), h);
    }

    public static void main(String[] args) {
        BaseBuilder bingBuilder = new BingBuilder();
        BaseBuilder proxyObject = getJdkProxyObject(bingBuilder.getClass(), (proxy, method, args1) -> {
            System.out.println("proxy对象生成");
            return method.invoke(bingBuilder, args1);
        });
        proxyObject.build();

    }
}
