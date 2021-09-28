package com.yb.fish.io;

public class BufferedInputStream extends InputStream{
    private InputStream inputStream;
    public BufferedInputStream (InputStream inputStream){
        this.inputStream = inputStream;
    }
    public void read(){
        inputStream.read();
        System.out.println("BufferedInputStream read");
    }
}
