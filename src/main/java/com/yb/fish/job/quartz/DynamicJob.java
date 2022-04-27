package com.yb.fish.job.quartz;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;


public class DynamicJob {
    /**
     * 待执行job的class名称
     **/
    private String jobClassName;
    /**
     * 任务组名称（可以用来区分唯一任务）
     **/
    private String bizJobId;
    /**
     * cron表达式
     **/
    private String cronExpression;
    /**
     * 定时任务待传参数
     **/
    private JobDataMap data;

    public String getJobClassName() {
        return jobClassName;
    }

    public String getBizJobId() {
        return bizJobId;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public JobDataMap getData() {
        return data;
    }
    public static Builder builder() {
        return new Builder();
    }

    public DynamicJob(Builder builder) {
        this.jobClassName = builder.jobClassName;
        this.bizJobId = builder.bizJobId;
        this.cronExpression = builder.cronExpression;
        this.data = builder.data;
    }


    public static class Builder {
        String jobClassName;
        String bizJobId;
        String cronExpression;
        JobDataMap data;

        public Builder jobClassName(String jobClassName) {
            this.jobClassName = jobClassName;
            return this;
        }

        public Builder bizJobId(String bizJobId) {
            this.bizJobId = bizJobId;
            return this;
        }

        public Builder cronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
            return this;
        }

        public Builder data(JobDataMap data) {
            this.data = data;
            return this;
        }

        public DynamicJob build() {
            if (StringUtils.isBlank(jobClassName)) {
                throw new RuntimeException("jobClassName is null");
            }
            if (StringUtils.isBlank(bizJobId)) {
                throw new RuntimeException("bizJobId is null");
            }
            if (StringUtils.isBlank(cronExpression)) {
                throw new RuntimeException("cronExpression is null");
            }
            return new DynamicJob(this);
        }
    }
}
