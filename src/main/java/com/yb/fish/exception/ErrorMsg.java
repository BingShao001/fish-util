package com.yb.fish.exception;

/**
* 统一提示
* @author bing
* @create 2018/1/26
* @version 1.0
**/
public enum ErrorMsg {
    SYSTEM_ERROR(500,"系统异常"),
    PARAM_ERROR(201,"入参数有误"),
    NON_DATA(302,"没有返回值");
    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    ErrorMsg(int errorCode, String errorMsg) {

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
