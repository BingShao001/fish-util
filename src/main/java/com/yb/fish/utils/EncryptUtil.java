package com.yb.fish.utils;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * @author 作者 E-mail: Bing.Shao
 * @version 1.0
 * @date 创建时间：2015-12-9 下午5:31:13
 * @parameter 对称密钥标示 ：DES 3DES ADES
 * @return
 */
public class EncryptUtil {
    /**
     * @param key 对称密钥标示 ：DES 3DES ADES
     *            初始化密钥
     * @return 密钥
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getKeys(String key) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(key);
        int places = 0;
        if ("DES".equals(key)) {
            places = 64;
        } else if ("3DES".equals(key)) {
            places = 156;
        } else if ("AES".equals(key)) {
            places = 128;
        }
        generator.init(places);
        SecretKey secretKey = generator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 对称解密
     *
     * @param data 明文
     * @param keys 密钥
     * @param key  加密算法形式
     * @return encryption
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] keys, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(keys, key);
        Cipher cipher = Cipher.getInstance(key);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryption = cipher.doFinal(data);
        return encryption;
    }

    /**
     * 对称解密
     *
     * @param data 明文
     * @param keys 密钥
     * @param key  加密算法形式
     * @return dencryption
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] keys, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(keys, key);
        Cipher cipher = Cipher.getInstance(key);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] dencryption = cipher.doFinal(data);
        return dencryption;
    }

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String AESencrypt(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return "";
        }
        byte[] bytes = data.getBytes();
        byte[] aesKey = EncryptUtil.getKeys("AES");
        byte[] aesResult = encrypt(bytes, aesKey, "AES");
        return aesResult.toString();
    }

    /**
     * 解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String AESdecrypt(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return "";
        }
        byte[] aesKey = EncryptUtil.getKeys("AES");
        SecretKey secretKey = new SecretKeySpec(aesKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] dencryption = cipher.doFinal(data.getBytes());
        return dencryption.toString();
    }

    public static void main(String[] args) throws Exception {
        //String DATA = "bing22352460@163.com";
        String DATA = System.currentTimeMillis() + "";
        String eData = AESencrypt(DATA);
//        System.out.println("AESencrypt : " + AESencrypt(eData));
//        System.out.println("AESdecrypt : "+ AESdecrypt(eData));
        /* Test AES */
        byte[] aesKey = EncryptUtil.getKeys("AES");
        System.out.println("AES KEY : " + aesKey);
        byte[] aesResult = EncryptUtil.encrypt(DATA.getBytes(), aesKey, "AES");
        System.out.println(DATA + ">>>AES 加密>>>" + aesResult);
        byte[] aesPlain = EncryptUtil.decrypt(aesResult, aesKey, "AES");
        System.out.println(DATA + ">>>AES 解密>>>" + new String(aesPlain));

        for (int i = 0;i<10;i++){
            byte[] aesKey1 = EncryptUtil.getKeys("AES");
            System.out.println("i : "+aesKey1);
        }
    }
}