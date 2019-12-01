package com.yb.fish.test;

import com.yb.fish.proxy.ProxyObjectFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author bing.zhang
 * @title: Main
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/1下午9:08
 */
public class Main {
    public static void main(String[] args) {
        JdkTestImpl JdkTestImpl = new JdkTestImpl();
        JdkTest jdkTestProxy = ProxyObjectFactory.getJdkProxyObject(JdkTest.class, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("print log.....");
                method.invoke(JdkTestImpl, args);
                return null;
            }
        });

        jdkTestProxy.say();
        CglibTest CglibTest = new CglibTest();
        CglibTest CglibTestProxy = ProxyObjectFactory.getCglibProxyObject(CglibTest.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("print log.....");
                method.invoke(CglibTest, objects);
                return null;
            }
        });
        CglibTestProxy.say();
    }
}
