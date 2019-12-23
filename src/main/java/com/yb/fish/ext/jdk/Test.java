package com.yb.fish.ext.jdk;

import sun.misc.Service;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author bing.zhang
 * @title: Test
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/23下午3:37
 */
public class Test {

    public static void main(String[] args) {
        Iterator<ExtService> providers = Service.providers(ExtService.class);
        ServiceLoader<ExtService> load = ServiceLoader.load(ExtService.class);

        while(providers.hasNext()) {
            ExtService ser = providers.next();
            ser.execute();
        }
        System.out.println("--------------------------------");
        Iterator<ExtService> iterator = load.iterator();
        while(iterator.hasNext()) {
            ExtService ser = iterator.next();
            ser.execute();
        }
    }
}
