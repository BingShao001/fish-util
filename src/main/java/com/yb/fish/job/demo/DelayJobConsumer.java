package com.yb.fish.job.demo;

import com.alibaba.fastjson.JSON;

import com.yb.fish.job.IBizJob;
import com.yb.fish.job.RedisDelayTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DelayJobConsumer {
    @Resource
    private RedisDelayTask redisDelayTask;

    @Scheduled(cron = "*/5 * * * * *")
    public void execute() {
        redisDelayTask.consumer("web1", new IBizJob() {
            @Override
            public void execute(String bizData) {
                //处理业务
                log.info("job_info : {}", JSON.toJSONString(bizData));
            }
        });
    }

}
