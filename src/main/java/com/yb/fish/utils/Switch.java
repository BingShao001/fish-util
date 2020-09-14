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
        int hashValue = Math.abs(key.hashCode()) % 100;
        System.out.println(hashValue);
        return requestExecutePercent >= hashValue;
    }

    public static void main(String[] args){
        Switch aSwitch = new Switch();
        aSwitch.hashControlPercent("zhangbing",10);
        aSwitch.hashControlPercent("shanglijie",10);
        aSwitch.hashControlPercent("55555555555",10);

    }

}
