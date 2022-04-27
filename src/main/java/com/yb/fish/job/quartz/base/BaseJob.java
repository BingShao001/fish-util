package com.yb.fish.job.quartz.base;


import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public abstract class BaseJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap data = context.getMergedJobDataMap();
        this.doJob(data);
    }
    protected abstract void doJob(JobDataMap data);
}