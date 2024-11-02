package com.yb.fish.job.delay;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DelayShardComponent {
    public static final String DELAY = "delay_";
    private final Map<String, Integer> delayShardConfig;

    public DelayShardComponent(String delayShardConfigValue) {
        this.delayShardConfig = JSON.parseObject(delayShardConfigValue, Map.class);
    }

    public String generateShardKey(String bizCode, String jobId) {
        int shard = delayShardConfig.getOrDefault(bizCode, 4);
        int shardIndex = Math.abs(jobId.hashCode() % shard);
        String shardKey = new StringBuilder(DELAY)
                .append(bizCode)
                .append("_")
                .append(shardIndex).toString();
        return shardKey;
    }

    public List<String> getShardKeys(String bizCode) {
        int shardCount = delayShardConfig.getOrDefault(bizCode, 4);
        List<String> shardKeys = new ArrayList<>();
        for (int i = 0; i < shardCount; i++) {
            shardKeys.add(DELAY + bizCode + "_" + i);
        }
        return shardKeys;
    }

}