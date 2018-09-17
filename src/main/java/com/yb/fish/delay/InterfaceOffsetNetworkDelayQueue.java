package com.yb.fish.delay;

import org.aspectj.lang.ProceedingJoinPoint;

/**
* 接口形式RPC补偿队列【需自己实现】
* @author bing
* @create 2018/7/2
* @version 1.0
**/
public interface InterfaceOffsetNetworkDelayQueue {
     static final int MAX = 2;
     void offsetTask(ProceedingJoinPoint jPoint);
     void syncOffsetData(ProceedingJoinPoint jPoint);
}
