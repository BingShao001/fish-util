package com.yb.fish.delay;

import com.yb.fish.executor.AsynTaskExecutors;
import com.yb.osp.core.exception.OspException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ThreadPoolExecutor;

public class OffsetNetworkDelayQueue {
    private static DelayQueue<Delayed> delayQueue = new DelayQueue<>();

    /**
     * 制定delay rule
     *
     * @param delayed
     */
    private void addDelayQueue(Delayed delayed) {
        delayQueue.add(delayed);
    }

    /**
     * 处理delay任务
     * @throws OspException
     */
    private boolean consumeAction(Class clazz,String methodName) throws OspException {
        Object returnData = null;
        Object param = null;
        //this is blocker
        while (!delayQueue.isEmpty()) {
            try {
                EventOffsetDelay eventOffsetDelay = (EventOffsetDelay) delayQueue.take();
                param = eventOffsetDelay.getDelayData();
                returnData = executeMethodByName(clazz,methodName,param);

            } catch (Exception e) {
                e.printStackTrace();
                offsetRpcTask(clazz,methodName);
            }

        }
        return null == returnData ? false : true;
    }

    /**
     * executeMethodByName
     * @param clazz
     * @param methodName
     * @param param
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object executeMethodByName(Class clazz,String methodName,Object... param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(methodName);
        Object returnData = method.invoke(param);
        return returnData;
    }
    /**
     * 补偿多线程延时调用
     *
     * @return
     * @throws Exception
     */
    public void offsetRpcTask(Class interfaceClazz,String methodName,Object... param) throws OspException {
        ThreadPoolExecutor threadPoolExecutor = AsynTaskExecutors.getExecutors();
        threadPoolExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                EventOffsetDelay offsetDelay = new EventOffsetDelay(System.currentTimeMillis() + EventOffsetDelay.DELAY_TIME, param);
                addDelayQueue(offsetDelay);
                return consumeAction(interfaceClazz,methodName);
            }
        });

        threadPoolExecutor.shutdown();
    }
}
