package com.instahela.deni.mkopo.test;

import android.util.Base64;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Test2 {

    public static Key publicKey;
    public static Key privateKey;

    //创建公私钥对
    public static void createKeyPair() throws Exception {
        //使用RSA算法获得密钥对生成器对象keyPairGenerator
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //设置密钥长度为1024
        keyPairGenerator.initialize(1024);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //获取公钥
        publicKey = keyPair.getPublic();
        //获取私钥
        privateKey = keyPair.getPrivate();


    }

    //RSA加密
    public static String encryptWithRSA(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] secret = cipher.doFinal(str.getBytes());
       return Base64.encodeToString(secret, Base64.NO_WRAP);
    }

    //RSA解密
    public static String decryptWithRSA(String secret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //传递私钥，设置为解密模式。
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //解密器解密由Base64解码后的密文,获得明文字节数组
        byte[] b = cipher.doFinal(Base64.decode(secret, Base64.NO_WRAP));
        //转换成字符串
        return new String(b);

    }

}
