package com.yb.fish.io;

public class FileInputStream extends InputStream {
    private String path;

    public FileInputStream(String path) {
        this.path = path;
    }

    public void read() {
        System.out.println("通过 " + path + "读取文件字节数据");
    }
}
