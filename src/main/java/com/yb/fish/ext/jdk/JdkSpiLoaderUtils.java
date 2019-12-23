package com.yb.fish.ext.jdk;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author bing.zhang
 * @title: JdkSpiLoaderUtils
 * @projectName common-yb-fish-utils
 * @description: jdk spi执行
 * @date 2019/12/23下午3:08
 */
public class JdkSpiLoaderUtils {

    public static void main(String[] args) {

        Iterator<ExtService> spiIterator = getServiceLoaderIterator(ExtService.class);
        while (spiIterator.hasNext()) {
            spiIterator.next().execute();
        }
    }

    /**
     * 获取符合扩展点条件类型的集合
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Iterator<T> getServiceLoaderIterator(Class<T> clazz) {
        ServiceLoader<T> spiLoader = ServiceLoader.load(clazz);
        Iterator<T> spiIterator = spiLoader.iterator();
        return spiIterator;
    }
}
