package com.yb.fish.io;

public class Main {
    public static void main(String[] args){
      //基础组件
      FileInputStream fileInputStream = new FileInputStream("/a.txt");
      //装饰器
      DataInputStream dataInputStream = new DataInputStream(fileInputStream);
      //装饰器
      BufferedInputStream bufferedInputStream = new BufferedInputStream(dataInputStream);
      bufferedInputStream.read();
    }
}
