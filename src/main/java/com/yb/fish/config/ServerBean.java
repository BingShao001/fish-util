package com.yb.fish.config;

/**
 * @author bing.zhang
 * @title: ServerBean
 * @projectName common-yb-fish-utils
 * @description: TODO
 * @date 2020/1/7下午5:25
 */
public class ServerBean {
    private String serverName;
    private String id;

    //init method
    public void init() {
        System.out.println("bean ServerBean init.");
        System.out.println("[Service]=>" + serverName + " id => "+id);
    }

    @Override
    public String toString() {
        return "[Service]=>" + serverName + " id => "+id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getId() {
        return id;
    }

    public ServerBean setId(String id) {
        this.id = id;
        return this;
    }
}
