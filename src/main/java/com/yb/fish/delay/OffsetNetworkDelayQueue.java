package com.yb.fish.delay;

import com.alibaba.fastjson.JSON;
import com.yb.fish.executor.AsynTaskExecutors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ThreadPoolExecutor;

import static com.yb.fish.constant.FishContants.ZERO;


@Service
public class OffsetNetworkDelayQueue implements InterfaceOffsetNetworkDelayQueue {

    private Map<String, Integer> counts = new ConcurrentHashMap<>();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    Logger logger = org.slf4j.LoggerFactory.getLogger(InterfaceOffsetNetworkDelayQueue.class);

    /**
     * 制定delay rule
     *
     * @param jPoint
     */
    private void addDelayQueue(DelayQueue<Delayed> delayQueue,ProceedingJoinPoint jPoint) {
        Object[] params = jPoint.getArgs();
        EventOffsetDelay offsetDelay = new EventOffsetDelay(System.currentTimeMillis() + EventOffsetDelay.DELAY_TIME, params);
        delayQueue.add(offsetDelay);
    }

    /**
     * 处理delay任务
     *
     */
    private boolean consumeAction(DelayQueue<Delayed> delayQueue,ProceedingJoinPoint jPoint) {
        Object returnData = null;
        String className = jPoint.getTarget().getClass().getName();
        String methodName = jPoint.getSignature().getName();
        String countKey = Thread.currentThread().getName()+className + methodName;
        this.increaseCount(countKey);
        //this is blocker
        while (!delayQueue.isEmpty()) {
            if (this.getCount(countKey) > MAX) {
                System.out.println(Thread.currentThread().getName()+" count : "+this.getCount(countKey));
                this.syncOffsetData(jPoint);
                try {
                    delayQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                EventOffsetDelay<Object[]> eventOffsetDelay = (EventOffsetDelay<Object[]>) delayQueue.take();
                Object[] params = eventOffsetDelay.getDelayData();
                returnData = jPoint.proceed(params);
            } catch (Throwable throwable) {
                backUp(jPoint);
            }
        }
        return null == returnData ? false : true;
    }

    private boolean backUp(ProceedingJoinPoint jPoint) {
        DelayQueue<Delayed> delayQueue = new DelayQueue<>();
        addDelayQueue(delayQueue,jPoint);
        return consumeAction(delayQueue,jPoint);
    }

    /**
     * 补偿多线程延时调用
     *
     * @return
     * @throws Exception
     */
    @Override
    public void offsetTask(ProceedingJoinPoint jPoint) {
        ThreadPoolExecutor threadPoolExecutor = AsynTaskExecutors.getExecutors();
        threadPoolExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return backUp(jPoint);
            }
        });
        threadPoolExecutor.shutdown();
    }

    /**
     * 持久化日志，注意事务外部配置
     *
     * @param jPoint
     */
    @Override
    public void syncOffsetData(ProceedingJoinPoint jPoint) {
        System.out.println(Thread.currentThread().getName());
        try {
            Class clazz = jPoint.getTarget().getClass();
            String className = clazz.getName();
            Object[] args = jPoint.getArgs();
            String argsJson = JSON.toJSONString(args);
            String realMethod = jPoint.getSignature().getName();
            String sql = "INSERT INTO offset (`class_name`,`method_name`,`args`,`create_time`) VALUES(?, ?, ?,now())";
            jdbcTemplate.update(sql, className, realMethod, argsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getCount(String countKey) {
        return counts.get(countKey) == null ? ZERO : counts.get(countKey);
    }

    private void increaseCount(String countKey) {
        int count = getCount(countKey);
        counts.put(countKey, ++count);
    }
}
