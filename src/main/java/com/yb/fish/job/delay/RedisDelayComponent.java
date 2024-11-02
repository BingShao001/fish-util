package com.yb.fish.job.delay;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RedisDelayComponent {

    Logger logger = LoggerFactory.getLogger(RedisDelayComponent.class);

    private final StringRedisTemplate redisTemplate;

    private final DelayShardComponent delayShardComponent;

    public RedisDelayComponent(StringRedisTemplate redisTemplate, DelayShardComponent delayShardComponent) {
        this.redisTemplate = redisTemplate;
        this.delayShardComponent = delayShardComponent;
    }


    public boolean addJob(DelayJob delayJob) {
        String bizCode = delayJob.getBizCode();
        String jobId = delayJob.getJobId();
        String shardKey = delayShardComponent.generateShardKey(bizCode, jobId);

        String luaScript =
                "local jobKey = KEYS[1] " +
                        "local shardKey = KEYS[2] " +
                        "local jobData = ARGV[1] " +
                        "local triggerTime = tonumber(ARGV[2]) " +
                        "if redis.call('SETNX', jobKey, jobData) == 1 then " +
                        "   return redis.call('ZADD', shardKey, triggerTime, jobKey) " +
                        "else " +
                        "   return 0 " +
                        "end";

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

        // Convert delay time to seconds as a float string
        long triggerTimeInSeconds = System.currentTimeMillis() / 1000 + delayJob.getDelayTime();

        Long result = redisTemplate.execute(redisScript,
                Arrays.asList(jobId, shardKey),
                delayJob.getData(), String.valueOf(triggerTimeInSeconds));
        return result != null && result > 0;
    }

    public void delJob(String bizCode, String jobId) {
        String shardBizCode = delayShardComponent.generateShardKey(bizCode, jobId);
        this.doDelJob(shardBizCode, jobId);
    }

    private boolean doDelJob(String shardBizCode, String jobId) {
        String luaScript =
                "local removed = redis.call('ZREM', KEYS[1], ARGV[1]) " +
                        "if removed == 1 then " +
                        "   redis.call('DEL', KEYS[2]) " +
                        "   return 1 " +
                        "else " +
                        "   return 0 " +
                        "end";

        DefaultRedisScript<Long> script = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisTemplate.execute(script, Arrays.asList(shardBizCode, jobId), jobId);

        return result != null && result == 1;
    }

    /**
     * 配合
     *
     * @param bizCode
     * @param bizJob
     * @see @ClusterScheduled
     */

    public void scheduleConsumer(String bizCode, IBizJob bizJob) {
        List<String> shardKeys = delayShardComponent.getShardKeys(bizCode);
        for (String shardKey : shardKeys) {
            doConsumer(shardKey, bizJob);
        }
    }


    private void doConsumer(String shardKey, IBizJob bizJob) {
        long now = System.currentTimeMillis() / 1000;
        Set<String> jobIds = redisTemplate.opsForZSet().rangeByScore(shardKey, 0, now);
        if (CollectionUtils.isEmpty(jobIds)) {
            return;
        }
        for (String jobId : jobIds) {
            String data = redisTemplate.opsForValue().get(jobId);
            if (StringUtils.isBlank(data)) {
                continue;
            }
            try {
                bizJob.execute(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //建议业务消费侧自己做幂等处理，这里如果消费任务没做删除，任务会有可能重复消费
            this.doDelJob(shardKey, jobId);
        }
    }
}

