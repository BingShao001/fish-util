package com.yb.fish.ability.enums;

public enum HttpMethodEnum {
    GET("get"),
    POST("post");

    private String value;

    HttpMethodEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public HttpMethodEnum setValue(String value) {
        this.value = value;
        return this;
    }
}
