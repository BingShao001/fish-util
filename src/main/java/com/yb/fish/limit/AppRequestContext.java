package com.yb.fish.limit;

import lombok.Builder;
import lombok.Data;

/**
 * @author hezhiyong
 * @date 2021/12/15
 * @description: 客户端请求上下文
 */
public class AppRequestContext {

    private ThreadLocal<AppRequestAttr> threadLocal = new ThreadLocal<AppRequestAttr>();

    public void set(AppRequestAttr consoleRequestAttr){
        threadLocal.set(consoleRequestAttr);
    }
    public AppRequestAttr get(){
        return threadLocal.get();
    }

    public void destroy(){
        threadLocal.remove();
    }

    @Builder
    @Data
    public static class AppRequestAttr {
        private Long userId;
        private String platform;
        private String version;
        private String headerLang; // header携带的lang,小写
        private String guid;
        private String appArea;
        private JwtUser jwtUser;
        private String sourceAppName;
        private String country; // 手机系统设置国家
        private String realIp; // 用户 IP
        private String deviceId; // 设备 ID
        private String sysVersion; // 设备系统版本
    }
}
