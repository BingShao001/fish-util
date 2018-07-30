package com.yb.fish.exception;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class TestMain {
    public static void main(String[] args){
        Properties props = System.getProperties();
        System.out.println("操作系统的名称：" + props.getProperty("os.name"));
        System.out.println("操作系统的版本：" + props.getProperty("os.version"));
        String ipAdd = "";
            try {
                ipAdd = InetAddress.getLocalHost().getHostAddress();
                System.out.println(ipAdd);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
}
