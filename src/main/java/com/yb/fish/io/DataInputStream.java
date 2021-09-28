package com.yb.fish.io;
/**
* DataInputStream装饰器之一
* @author bing
* @create 28/09/2021
* @version 1.0
**/
public class DataInputStream extends InputStream{
    private InputStream inputStream;
    public DataInputStream (InputStream inputStream){
        this.inputStream = inputStream;
    }
    public void read(){
        inputStream.read();
        System.out.println("DataInputStream read");
    }
}
