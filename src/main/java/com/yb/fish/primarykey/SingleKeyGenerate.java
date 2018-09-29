package com.yb.fish.primarykey;

import com.yb.fish.constant.FishContants;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * (单点)主键生成工具
 *
 * @author bing
 * @version 1.0
 * @create 2018/3/16
 **/
public class SingleKeyGenerate {

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
        int randomNum = ThreadLocalRandom.current().nextInt() * FishContants.BOUND;
        return randomNum < FishContants.MIN_NUM ? randomNum + FishContants.MIN_NUM : randomNum;
    }

}