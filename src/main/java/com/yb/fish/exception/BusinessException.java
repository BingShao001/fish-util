package com.yb.fish.exception;


/**
* 标识业务异常
* @author bing
* @create 2018/5/23
* @version 1.0
**/
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;
    /**
     * 默认业务错误分组
     */
    private final static String BUSSINESS_DEFAULT_ERRORGROUP = "220";
    /**
     * 错误编码
     */
    private int errorCode;
    /**
     * 错误消息
     */
    private String message;
    /**
     * 业务错误分组
     */
    private String bussinessErrorGroup = BusinessException.BUSSINESS_DEFAULT_ERRORGROUP;


    /**
     * 构造一个分组业务异常.
     * @param errorCode 错误码
     * @param message 信息描述
     * @param bussinessErrorGroup 错误分组
     */
    public BusinessException(int errorCode, String message, String bussinessErrorGroup ) {

        this.errorCode = errorCode;
        this.message = message;
        this.bussinessErrorGroup = bussinessErrorGroup;
    }

    /**
     * 构造一个默认分组业务异常.
     * @param errorCode 错误码
     * @param message 信息描述
     */
    public BusinessException(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getBussinessErrorGroup() {
        return bussinessErrorGroup;
    }

    public void setBussinessErrorGroup(String bussinessErrorGroup) {
        this.bussinessErrorGroup = bussinessErrorGroup;
    }

}
