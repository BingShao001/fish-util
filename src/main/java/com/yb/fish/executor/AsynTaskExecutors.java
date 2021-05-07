package com.yb.fish.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务线程池
 *
 * @author bing
 * @version 1.0
 * @create 2018/3/6
 **/
public class AsynTaskExecutors {
    public static final String ASYNC_THREAD_POOL_PROPERTIES = "asyncThreadPool.properties";
    public static final String CORE_POOL_SIZE = "corePoolSize";
    public static final String MAXIMUM_POOL_SIZE = "maximumPoolSize";
    public static final String KEEP_ALIVE_TIME = "keepAliveTime";
    public static final String WORK_QUEUE_SIZE = "workQueueSize";
    public static ThreadPoolExecutor threadPoolExecutor = null;

    private static final Logger logger = LoggerFactory.getLogger(AsynTaskExecutors.class);
    private AsynTaskExecutors() {
    }
    static Properties setting = null;
    static {
        setting  = new Properties();
        try {
            setting.load(AsynTaskExecutors.class.getClassLoader().getResourceAsStream(ASYNC_THREAD_POOL_PROPERTIES));
        } catch (IOException e) {
            logger.info("AsynTaskExecutors IOException={}",e);
        }
        int corePoolSize = Integer.parseInt(setting.getProperty(CORE_POOL_SIZE));
        int maximumPoolSize = Integer.parseInt(setting.getProperty(MAXIMUM_POOL_SIZE));
        long keepAliveTime = Long.parseLong(setting.getProperty(KEEP_ALIVE_TIME));
        int workQueueSize = Integer.parseInt(setting.getProperty(WORK_QUEUE_SIZE));
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(workQueueSize), new ThreadPoolExecutor.CallerRunsPolicy());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {threadPoolExecutor.shutdown();}));
    }

    /**
     * 有边界连接池对象
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getExecutors(){
        return threadPoolExecutor;
    }

    public static void main(String[] args){
        ThreadPoolExecutor threadPoolExecutor1 = AsynTaskExecutors.getExecutors();
        ThreadPoolExecutor threadPoolExecutor2 = AsynTaskExecutors.getExecutors();
        System.out.println(threadPoolExecutor1);
        System.out.println(threadPoolExecutor2);

    }
}