package com.yb.fish.init;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author bing.zhang
 * @title: LocalLimitConfigContainer
 * @projectName common-yb-fish-utils
 * @description: LocalLimitConfigContainer
 * @date 2020/1/2下午3:36
 */
public class LocalLimitConfigContainer {

    private static Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
    private static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    public static RateLimiter getRateLimiter(String className) {
        return rateLimiterMap.get(className);
    }


    public static Semaphore getSemaphore(String className) {
        return semaphoreMap.get(className);
    }

    public static void setRateLimiter(String className, RateLimiter rateLimiter) {
        if (null == rateLimiterMap) {
            rateLimiterMap = new ConcurrentHashMap<>();
        }
        rateLimiterMap.put(className, rateLimiter);
    }

    public static void setSemaphore(String className, Semaphore semaphore) {
        if (null == semaphoreMap) {
            semaphoreMap = new ConcurrentHashMap<>();
        }
        semaphoreMap.put(className, semaphore);
    }
}