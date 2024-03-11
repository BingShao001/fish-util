package com.yb.fish.primarykey;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * SnowFlake 雪花算法，修改了部分逻辑
 * 先不用，hostname可能取不到
 * @author bing
 * @version 1.0
 * @create 2024/2/28
 **/
@Deprecated
@Slf4j
public class HostNameBasedSnowFlake {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;
    private String hostName = null;
    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId = 1L;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    // 静态内部类持有单例实例
    private static class SingletonHolder {
        private static final HostNameBasedSnowFlake INSTANCE = new HostNameBasedSnowFlake();
    }

    // 获取单例实例的方法
    public static HostNameBasedSnowFlake getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HostNameBasedSnowFlake() {
        try {
            // 获取本地主机的 InetAddress 对象
            InetAddress localhost = InetAddress.getLocalHost();
            // 获取主机名
            this.hostName = localhost.getHostName();

        } catch (UnknownHostException e) {
            // 处理异常
            log.error("get hostname error.");
        }
        machineId = this.parseNumByHostName();
        if (0 == machineId) {
            machineId++;
        }
        if (machineId > 31) {
            datacenterId++;
            if (datacenterId > 31) {
                throw new RuntimeException("datacenterId oversize error value : "+datacenterId);
            }
        }
        log.info("init datacenterId {} machineId value {}", datacenterId, machineId);
        this.initId(datacenterId, machineId);
    }
    private void parseLast12bitByIP(){
        try {
            // 获取本机IP地址
            InetAddress ipAddress = InetAddress.getLocalHost();
            byte[] ipBytes = ipAddress.getAddress();

            // 获取后12位
            int last12Bits = 0;
            for (int i = 1; i <= 3; i++) {
                last12Bits = (last12Bits << 8) | (ipBytes[ipBytes.length - i] & 0xFF);
            }

            System.out.println("后12位：" + Integer.toBinaryString(last12Bits));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    private long parseNumByHostName() {
        if (StringUtils.isBlank(this.hostName) || this.hostName.length() < 2) {
            log.warn("hostname is null.");
            return getRandomNum();
        }
        // 获取字符串的后两位字符
        int length = hostName.length();
        char lastChar = hostName.charAt(length - 1);
        char secondLastChar = hostName.charAt(length - 2);
        if (Character.isDigit(secondLastChar)) {
            // 截取字符串，去除后两位字符
            return Long.valueOf(hostName.substring(length - 2));
        }
        if (Character.isDigit(lastChar)) {
            return Long.valueOf(hostName.substring(length - 1));
        }
        return getRandomNum();
    }

    private long getRandomNum() {
        Random random = new Random();
        return random.nextInt(16) + 1;
    }

    public HostNameBasedSnowFlake(long datacenterId, long machineId) {
        this.initId(datacenterId, machineId);
    }

    private void initId(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();


        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大，进入下一周期
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
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

    public static void main(String[] args) {
//            this.hostName = "xxx-service-18";
//            this.hostName = "xxx-service-17";
//            this.hostName = "xxx-service-0";
//            this.hostName = "xxx-service-1";
//            this.hostName = "xxx-service-16";
        HostNameBasedSnowFlake hostNameBasedSnowFlake = HostNameBasedSnowFlake.getInstance();
        hostNameBasedSnowFlake.parseLast12bitByIP();
        for (int i = 0; i < (1 << 12); i++) {
            System.out.println(hostNameBasedSnowFlake.nextId());
        }
        System.out.println("960543929923698762".length());
    }
}
