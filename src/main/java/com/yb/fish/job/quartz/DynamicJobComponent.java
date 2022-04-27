package com.yb.fish.job.quartz;


import com.yb.fish.job.quartz.base.BaseJob;
import com.yb.fish.job.utils.JiuweiSpringBeanUtil;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@Component
public class DynamicJobComponent {
    private static Logger log = LoggerFactory.getLogger(DynamicJobComponent.class);
    @Resource
    private Scheduler scheduler;

    public void addCronJob(DynamicJob dynamicJob) throws SchedulerException {
            scheduler.start();
            String jobClassName = dynamicJob.getJobClassName();
            BaseJob baseJob = (BaseJob) JiuweiSpringBeanUtil.getBean(jobClassName);
            JobDetail jobDetail = JobBuilder.newJob(baseJob.getClass()).
                    withIdentity(jobClassName, dynamicJob.getBizJobId())
                    .build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(dynamicJob.getCronExpression());
            CronTrigger trigger = TriggerBuilder.newTrigger().
                     withIdentity(dynamicJob.getJobClassName(), dynamicJob.getBizJobId())
                    .withSchedule(scheduleBuilder)
                    .usingJobData(dynamicJob.getData())
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 任务暂停
     *
     * @param jobClassName
     * @param bizJobId
     * @throws Exception
     */
    public void jobPause(String jobClassName, String bizJobId) throws Exception {
        scheduler.pauseJob(JobKey.jobKey(jobClassName, bizJobId));
    }

    /**
     * 恢复任务
     *
     * @param jobClassName
     * @param bizJobId
     * @throws Exception
     */
    public void jobResume(String jobClassName, String bizJobId) throws Exception {
        scheduler.resumeJob(JobKey.jobKey(jobClassName, bizJobId));
    }

    /**
     * 删除任务
     *
     * @param jobClassName
     * @param bizJobId
     * @throws Exception
     */
    public void jobDel(String jobClassName, String bizJobId) throws Exception {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, bizJobId));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, bizJobId));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, bizJobId));
    }

}
