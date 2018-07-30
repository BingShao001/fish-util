package com.yb.fish.delay;

import com.yb.osp.core.exception.OspException;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
* 接口形式RPC补偿队列【需自己实现】
* @author bing
* @create 2018/7/2
* @version 1.0
**/
public interface InterfaceOffsetNetworkDelayQueue {
    public static DelayQueue<Delayed> delayQueue = new DelayQueue<>();
    /**
     * 制定delay规则
     *
     * @param delayed
     */
    public void addDelayQueue(Delayed delayed);

    /**
     * 处理delay任务
     * @throws OspException
     */
    public void consumeAction() throws OspException;

    /**
     * 补偿多线程延时调用
     *
     * @return
     * @throws Exception
     */
    public void offsetRpcTask(final Object data);
}
