package com.yb.fish.utils;

import com.yb.fish.exception.OriginalAssert;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

/**
 * RequestProperty 请求元素
 *
 * @author bing
 * @version 1.0
 * @create 2023/7/25
 **/
public class RequestProperty {

    private String url;

    private HttpMethod method;

    private Map<String, Object> paramMap;

    private HttpHeaders httpHeaders;

    private int timeout;

    public int getTimeout() {
        return timeout;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * 有惨
     * @param builder 参数
     */
    public RequestProperty(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.paramMap = builder.paramMap;
        this.timeout = builder.timeout;
        this.httpHeaders = builder.httpHeaders;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private static final int DEFAULT_TIMEOUT = 5000;
        private String url;
        private HttpMethod method = HttpMethod.GET;
        private Map<String, Object> paramMap;
        private int timeout = DEFAULT_TIMEOUT;
        private HttpHeaders httpHeaders;


        /**
         * 组装请求参数
         *
         * @param key   入参标识
         * @param value 入参值
         * @return this
         */
        public Builder constructParam(String key, Object value) {
            //一般请求参数不会超过6个
            Map<String, Object> paramMap = null == this.paramMap ? new HashMap<>(8) : this.paramMap;
            paramMap.put(key, value);
            this.paramMap = paramMap;
            return this;
        }

        /**
         * 完成组装请求参数
         *
         * @return this
         */
        public Builder finishConstructParam() {
            OriginalAssert.isEmpty(this.paramMap, "param is empty.");
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder httpHeaders(HttpHeaders httpHeaders) {
            this.httpHeaders = httpHeaders;
            return this;
        }

        /**
         * 构建
         *
         * @return RequestProperty
         */
        public RequestProperty build() {
            OriginalAssert.isStringEmpty(url, "url is null.");
            //校验熟悉的逻辑

            return new RequestProperty(this);
        }
    }

    @Override
    public String toString() {
        return "RequestProperty{" + "url='" + url + '\'' + ", method=" + method + ", paramMap=" +
            paramMap + ", timeout=" + timeout + '}';
    }
}
