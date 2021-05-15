package com.yb.fish.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class Switch {

    private ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    /**
     * 按请求流量百分比切流
     *
     * @param requestExecutePercent 流量的百分比
     * @return
     */
    public boolean controlPercentRequest(String requestExecutePercent) {
        int randomValue = threadLocalRandom.nextInt(100) + 1;
        return randomValue <= Integer.parseInt(null == requestExecutePercent ? "0" : requestExecutePercent);
    }

    public boolean hashControlPercent(String key, int requestExecutePercent) {
        int hashValue = (key.hashCode() & 0x7FFFFFFF) % 100;
        System.out.println(hashValue);
        return requestExecutePercent >= hashValue;
    }
}
