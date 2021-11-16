package com.yb.fish.job;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class RedisDelayTask {

    public static final int FAIL = 0;

    private RedisTemplate redisTemplate;

    public RedisDelayTask(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean addJob(DelayJob delayJob) {
        return redisTemplate.opsForZSet().addIfAbsent(delayJob.getBizId(), delayJob.getJobId(), delayJob.getDelayTime()) &&
                redisTemplate.opsForValue().setIfAbsent(delayJob.getJobId(), delayJob.getData());
    }

    public boolean delJob(String bizId, String jobId) {
        return !Long.valueOf(FAIL).equals(redisTemplate.opsForZSet().remove(bizId, jobId)) && redisTemplate.delete(jobId);
    }

    public void consumer(String bizId, IBizJob bizJob) {
        long start = 0L;
        long now = System.currentTimeMillis();
        Set<String> jobIds = redisTemplate.opsForZSet().rangeByScore(bizId, start, now);
        if (CollectionUtils.isEmpty(jobIds)) {
            return;
        }
        for (String jobId : jobIds) {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String data = valueOperations.get(jobId);
            if (StringUtils.isBlank(data)) {
                continue;
            }
            bizJob.execute(data);
            redisTemplate.delete(jobId);
        }
        redisTemplate.opsForZSet().removeRange(bizId, start, now);
    }

}
