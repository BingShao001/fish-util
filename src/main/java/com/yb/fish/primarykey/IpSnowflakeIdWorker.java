package com.yb.fish.primarykey;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 唯品会雪花算法
 *
 * @author bing
 * @version 1.0
 * @create 2018/5/22
 **/
public class IpSnowflakeIdWorker {
    /**
     * 上次生成ID的时间截
     */
    private static long lastTimestamp = -1L;
    private static long digits = (long) Math.pow(10, 10);
    private static String maxIp = "10.255.255.255";
    private static String minIp = "10.0.0.0";

    private static long getApplicationId() {
        String currentIp = getCurrentApplicationIp();
        return getIPNum(currentIp);
    }

    private static String getCurrentApplicationIp() {
        String ipAdd = "";
        try {
            ipAdd = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAdd;
    }

    private static Long getIPNum(final String IP) {
        Long IPNum = 0L;
        final String IPStr = IP.trim();
        if (IPStr != null && IPStr.length() != 0) {
            final String[] subips = IPStr.split("\\.");
            for (final String str : subips) {
                // 向左移5位
                IPNum = IPNum << 3;
                IPNum += Integer.parseInt(str);
            }
        }
        return IPNum;
    }

    /**
     * 生成一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public static synchronized long generateId() {
        long timestamp = timeGen();
        long applicationId = getApplicationId();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的(单机并发)，则改变applicationId
        if (lastTimestamp == timestamp) {
            applicationId = applicationId + 1;
            //毫秒内序列溢出
            if (applicationId > getIPNum(maxIp)) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
                applicationId = getApplicationId();
            }
        }
        //上次生成ID的时间截
        lastTimestamp = timestamp;
        //移位并通过或运算拼到一起组成64位的ID
        return applicationId * digits + timestamp;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected static long timeGen() {
        return System.currentTimeMillis() >> 8;
    }

    public static void main(String[] args) {
        System.out.println(IpSnowflakeIdWorker.getIPNum(maxIp));
        System.out.println(timeGen());

        long id = generateId();
        System.out.println("id :"+id);
        String strLong = Long.toString(id);
        System.out.println("str : "+strLong);
        System.out.println("int :"+Long.parseLong(strLong));
    }
}
