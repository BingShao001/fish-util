package com.yb.fish.io;

public class DataInputStream extends InputStream{
    private InputStream inputStream;
    public DataInputStream (InputStream inputStream){
        this.inputStream = inputStream;
    }
    public void read(){
        System.out.println("DataInputStream read");
        inputStream.read();
    }
}
