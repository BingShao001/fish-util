package com.yb.fish.job.delay;


import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public class RedisDelayComponent {

    public static final int FAIL = 0;

    private RedisTemplate<String,String> lokiClient;

    public RedisDelayComponent(RedisTemplate lokiClient) {
        this.lokiClient = lokiClient;
    }

    public boolean addJob(DelayJob delayJob) {
        return lokiClient.opsForValue().setIfAbsent(delayJob.getJobId(), delayJob.getData()) &&
                lokiClient.opsForZSet().add(delayJob.getBizId(), delayJob.getJobId(), delayJob.getDelayTime());
    }

    public boolean delJob(String bizId,String jobId) {
        lokiClient.opsForZSet().remove(jobId);
        lokiClient.delete(jobId);
        return !Long.valueOf(FAIL).equals(lokiClient.opsForZSet().remove(bizId, jobId)) && lokiClient.delete(jobId);

    }

    /**
     * 配合@ClusterScheduled
     * @param bizId
     * @param bizJob
     */
    public void consumer(String bizId, IBizJob bizJob) {
        long start = 0L;
        long now = System.currentTimeMillis();
        ValueOperations<String, String> valueOperations = lokiClient.opsForValue();
        Set<String> jobIds = lokiClient.opsForZSet().rangeByScore(bizId, start, now);
        if (CollectionUtils.isEmpty(jobIds)) {
            return;
        }
        for (String jobId : jobIds) {
            String data = valueOperations.get(jobId);
            if (StringUtils.isBlank(data)) {
                continue;
            }
            lokiClient.delete(jobId);
            bizJob.execute(data);
        }
        lokiClient.opsForZSet().removeRange(bizId, start, now);
    }

}
