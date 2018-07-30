package com.yb.fish.primarykey;

import com.yb.fish.constant.FishContants;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * (单点)主键生成工具
 * @author bing
 * @create 2018/3/16
 * @version 1.0
 **/
public class SingleKeyGenerate {

    private static Lock lock = new ReentrantLock();

    /***
     * 生成 UUID
     * @return String
     */
    public static String generateStringKey() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        uuidStr = uuidStr.replace("-", "");
        return uuidStr;
    }

    /**
     * 生成长整型ID
     *
     * @return String
     */
    public static long generateLongKey() {
        return System.currentTimeMillis() + getRandomNum();
    }

    private static long getRandomNum() {
        Random random = new Random();
        int randomNum = FishContants.ZERO;
        try {
            lock.lock();
            randomNum = random.nextInt(FishContants.BOUND);
            if (randomNum < FishContants.MIN_NUM) {
                randomNum = randomNum + FishContants.MIN_NUM;
            }
        } finally {
            lock.unlock();
        }
        return randomNum;
    }

}