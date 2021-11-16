package com.yb.fish.job;

import org.apache.commons.lang3.StringUtils;

public class DelayJob {
    /**
     * 区分job维度
     */
    private String jobId;
    /**
     * 区分业务维度
     */
    private String bizId;
    /**
     * 任务延时时间（系统时间+延时时间）
     */
    private long delayTime;
    /**
     * 延时任务数据
     */
    private String data;

    public String getJobId() {
        return jobId;
    }

    public String getBizId() {
        return bizId;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public String getData() {
        return data;
    }


    DelayJob(Builder builder) {
        this.jobId = builder.jobId;
        this.bizId = builder.bizId;
        this.delayTime = builder.delayTime;
        this.data = builder.data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        /**
         * 区分job维度
         */
        String jobId;
        /**
         * 区分业务维度
         */
        String bizId;
        /**
         * 任务延时时间（系统时间+延时时间）
         */
        long delayTime;
        /**
         * 延时任务数据
         */
        String data;

        public Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder bizId(String bizId) {
            this.bizId = bizId;
            return this;
        }

        public Builder data(String data) {
            this.data = data;
            return this;
        }

        public Builder delayTime(long delayTime) {
            this.delayTime = delayTime;
            return this;
        }

        public DelayJob build() {
            if (StringUtils.isBlank(jobId)) {
                throw new RuntimeException("jobId is null");
            }
            if (StringUtils.isBlank(bizId)) {
                throw new RuntimeException("bizId is null");
            }
            if (StringUtils.isBlank(data)) {
                throw new RuntimeException("data is null");
            }
            return new DelayJob(this);
        }
    }
}
