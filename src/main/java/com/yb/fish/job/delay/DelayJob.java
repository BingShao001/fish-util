package com.yb.fish.job.delay;

import org.apache.commons.lang3.StringUtils;

public class DelayJob {
    /**
     * 区分业务维度
     * eg: close_order
     */
    private String bizCode;
    /**
     * 区分job维度
     * eg: orderId
     */
    private String jobId;
    /**
     * 任务延时时间（系统时间+延时时间）
     * eg: 超时关单的时间
     */
    private long delayTime;
    /**
     * 延时任务数据
     * eg: 可以传json字符串数据，用于延时任务执行
     */
    private String data;

    public String getJobId() {
        return jobId;
    }

    public String getBizCode() {
        return bizCode;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public String getData() {
        return data;
    }


    DelayJob(Builder builder) {
        this.jobId = builder.jobId;
        this.bizCode = builder.bizCode;
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
        String bizCode;
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

        public Builder bizCode(String bizCode) {
            this.bizCode = bizCode;
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
            if (StringUtils.isBlank(bizCode)) {
                throw new RuntimeException("bizCode is null");
            }
            if (StringUtils.isBlank(data)) {
                throw new RuntimeException("data is null");
            }
            return new DelayJob(this);
        }
    }
}
