package com.yb.fish.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
* 加密用的Key 可以用26个字母和数字组成
* 此处使用AES-128-CBC加密模式，key需要为16位。
* @author bing
* @create 2018/6/28
* @version 1.0
**/
public class AESOperator {
    private static AESOperator instance = null;

    private AESOperator() {

    }

    public static AESOperator getInstance() {
        if (instance == null){instance = new AESOperator();}
        return instance;
    }

    public static String base64Encrypt(String text){
        if (null == text){return null;}
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] bytes = null;
        try {
            bytes = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoder.encode(bytes);
    }

    public static String base64decode(String text){
        if (null == text){return null;}
        BASE64Decoder decoder = new BASE64Decoder();
        String context = null;
        try {
            context =  new String(decoder.decodeBuffer(text), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return context;
    }
    // 加密
    public static String encrypt(String sSrc) throws Exception {

        return base64Encrypt(sSrc);
    }

    // 解密
    public static String decrypt(String sSrc) throws Exception {

        return base64decode(sSrc);
    }
    public static void main(String[] args) throws Exception {
        String u = URLEncoder.encode(encrypt("12324555"),"UTF-8");
        System.out.println(u);
        try {
            System.out.println( decrypt(URLDecoder.decode(u, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
