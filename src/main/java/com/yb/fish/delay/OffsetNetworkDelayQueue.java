package com.yb.fish.delay;

import com.alibaba.fastjson.JSON;
import com.yb.fish.appcontext.SpringContextUtil;
import com.yb.fish.constant.FishContants;
import com.yb.fish.executor.AsynTaskExecutors;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ThreadPoolExecutor;

import static com.yb.fish.constant.FishContants.ZERO;


@Service
public class OffsetNetworkDelayQueue implements InterfaceOffsetNetworkDelayQueue {

    private Map<String, Integer> contains = new ConcurrentHashMap<>();
    Logger logger = org.slf4j.LoggerFactory.getLogger(InterfaceOffsetNetworkDelayQueue.class);
    @Resource
    SpringContextUtil springContextUtil;
    /**
     * 制定delay rule
     *
     * @param jPoint
     */
    private void addDelayQueue(DelayQueue<Delayed> delayQueue,ProceedingJoinPoint jPoint) {
        Object[] params = jPoint.getArgs();
        String className = jPoint.getTarget().getClass().getName();
        String methodName = jPoint.getSignature().getName();
        int currentTime = contains.get(Thread.currentThread().getName()+className + methodName+FishContants.TIME);
        int delayTime = currentTime == ZERO ? EventOffsetDelay.DELAY_TIME : currentTime;
        EventOffsetDelay offsetDelay = new EventOffsetDelay(System.currentTimeMillis() + delayTime, params);
        delayQueue.add(offsetDelay);
    }

    /**
     * 处理delay任务
     *
     */
    private boolean consumeAction(DelayQueue<Delayed> delayQueue,ProceedingJoinPoint jPoint,int tryNum,String taskSql) {
        Object returnData = null;
        String className = jPoint.getTarget().getClass().getName();
        String methodName = jPoint.getSignature().getName();
        String currentName = Thread.currentThread().getName()+className + methodName;
        String countKey = currentName+FishContants.COUNT;
        this.increaseCount(countKey);
        //this is blocker
        while (!delayQueue.isEmpty()) {
            int currentCount = this.getCount(countKey);
            if (this.getCount(countKey) > tryNum) {
                this.syncOffsetData(jPoint,taskSql);
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
                contains.put(currentName+FishContants.TIME,currentCount*EventOffsetDelay.DELAY_TIME);
            } catch (Throwable throwable) {
                backUp(jPoint,tryNum,taskSql);
            }
        }
        return null == returnData ? false : true;
    }

    private boolean backUp(ProceedingJoinPoint jPoint,int tryNum,String taskSql) {
        DelayQueue<Delayed> delayQueue = new DelayQueue<>();
        addDelayQueue(delayQueue,jPoint);
        return consumeAction(delayQueue,jPoint,tryNum,taskSql);
    }

    /**
     * 补偿多线程延时调用
     *
     * @return
     * @throws Exception
     */
    @Override
    public void offsetTask(ProceedingJoinPoint jPoint,int tryNum,String taskSql) {
        ThreadPoolExecutor threadPoolExecutor = AsynTaskExecutors.getExecutors();
        threadPoolExecutor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return backUp(jPoint,tryNum,taskSql);
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
    public void syncOffsetData(ProceedingJoinPoint jPoint,String taskSql) {
        Class clazz = jPoint.getTarget().getClass();
        String className = clazz.getName();
        String realMethod = jPoint.getSignature().getName();
        if (StringUtils.isBlank(taskSql)){
            logger.warn("syncOffsetData : service：{}",className+"."+realMethod);
            return;
        }
        try {
            Object[] args = jPoint.getArgs();
            String argsJson = JSON.toJSONString(args[FishContants.ZERO]);
            System.out.println(taskSql);
            JdbcTemplate jdbcTemplate = springContextUtil.getBean(JdbcTemplate.class);
            jdbcTemplate.update(taskSql, argsJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getCount(String countKey) {
        return contains.get(countKey) == null ? ZERO : contains.get(countKey);
    }

    private void increaseCount(String countKey) {
        int count = getCount(countKey);
        contains.put(countKey, ++count);
    }
}
