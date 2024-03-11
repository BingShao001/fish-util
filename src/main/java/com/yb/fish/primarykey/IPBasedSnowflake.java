package com.yb.fish.primarykey;

import lombok.extern.slf4j.Slf4j;

import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * IPBasedSnowflake
 * 把IP的后12位作为数据中心id（4位）机器号（8位）
 * 前面的20位通常代表网络地址，IPv4地址的后12位不同可以作为分片区分
 *
 * @author bing
 * @version 1.0
 * @create 2024/2/29
 **/
@Slf4j
public class IPBasedSnowflake {
    private final static long START_STMP = 1704038400000L; // 起始的时间戳，2024-01-01
    private final static long SEQUENCE_BIT = 10; // 序列号占用的位数
    private final static long MACHINE_BIT = 8; // 机器标识占用的位数
    private final static long DATACENTER_BIT = 4; // 数据中心占用的位数

    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;
    private long machineId;
    private long sequence = 0L;
    private long lastStmp = -1L;

    // 静态内部类持有单例实例
    private static class SingletonHolder {
        private static final IPBasedSnowflake INSTANCE = new IPBasedSnowflake();
    }

    // 获取单例实例的方法
    public static IPBasedSnowflake getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private IPBasedSnowflake() {
        try {
            // 获取本机IP地址
            String ipAddress = Objects.requireNonNull(IpV4Utils.getIpAddr());

            // 获取IP后12位作为机房ID和机器号
            long last12Bits = IpV4Utils.lastNBitsOfIp(ipAddress, 12);
            this.datacenterId = last12Bits >>> 8; // 取前4位作为数据中心ID
            this.machineId = last12Bits & 0xFF; // 取后8位作为机器号
            log.info("init datacenterId {} machineId {}", datacenterId, machineId);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public synchronized long nextId() {
        long currStmp = getNewstmp();
        // 处理时钟回拨，等待100ms时钟同步，临时方案待后续完善
        if (currStmp < lastStmp) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currStmp = getNewstmp();
            if (currStmp < lastStmp) {
                throw new RuntimeException("Clock moved backwards. node message，datacenterId " + datacenterId + " machineId " + machineId + " back time：" + (lastStmp - currStmp));
            }
        }

        if (currStmp == lastStmp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void runTest() {
        IPBasedSnowflake snowFlake = IPBasedSnowflake.getInstance();
        Set<Long> threadSafeHashSet = Collections.synchronizedSet(new HashSet<>());
        CountDownLatch countDownLatch = new CountDownLatch(100);
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                        long nextId = snowFlake.nextId();
                        System.out.println("work thread : " + Thread.currentThread().getName() + " *id* : " + nextId);
                        if (threadSafeHashSet.add(nextId)) {
                            throw new RuntimeException("重复id");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            });
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        IPBasedSnowflake.runTest();
    }
}

