package com.yb.fish.io;
/**
* 相当于默认的，最基础的实现
* @author bing
* @create 28/09/2021
* @version 1.0
**/
public class FileInputStream extends InputStream {
    private String path;

    public FileInputStream(String path) {
        this.path = path;
    }

    public void read() {
        System.out.println("通过 " + path + "读取文件字节数据");
    }
}
