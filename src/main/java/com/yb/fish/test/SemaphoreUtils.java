package com.yb.fish.test;

import java.util.concurrent.Semaphore;

/**
 * @author bing.zhang
 * @title: SemaphoreUtils
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2019/12/9上午10:30
 */
public class SemaphoreUtils {

    private SemaphoreUtils() {}

    public static Semaphore getSemaphoreInstance(int permits) {
        return new Semaphore(permits);
    }
}
