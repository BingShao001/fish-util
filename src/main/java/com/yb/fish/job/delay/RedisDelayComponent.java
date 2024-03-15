package com.yb.fish.job.delay;


import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class RedisDelayComponent {

    public static final int FAIL = 0;

    private RedisTemplate<String,String> redisTemplate;

    public RedisDelayComponent(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean addJob(DelayJob delayJob) {
        return redisTemplate.opsForValue().setIfAbsent(delayJob.getJobId(), delayJob.getData()) &&
                redisTemplate.opsForZSet().add(delayJob.getBizCode(), delayJob.getJobId(),
                        System.currentTimeMillis() + delayJob.getDelayTime());
    }

    public boolean delJob(String bizId,String jobId) {
        redisTemplate.opsForZSet().remove(jobId);
        redisTemplate.delete(jobId);
        return !Long.valueOf(FAIL).equals(redisTemplate.opsForZSet().remove(bizId, jobId)) && redisTemplate.delete(jobId);

    }

    /**
     * 配合@ClusterScheduled
     * @param bizCode
     * @param bizJob
     */
    public void consumer(String bizCode, IBizJob bizJob) {
        long start = 0L;
        long now = System.currentTimeMillis();
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Set<String> jobIds = redisTemplate.opsForZSet().rangeByScore(bizCode, start, now);
        if (CollectionUtils.isEmpty(jobIds)) {
            return;
        }
        for (String jobId : jobIds) {
            String data = valueOperations.get(jobId);
            if (StringUtils.isBlank(data)) {
                continue;
            }
            redisTemplate.delete(jobId);
            redisTemplate.opsForZSet().remove(bizCode, jobId);
            bizJob.execute(data);
        }
    }

}

