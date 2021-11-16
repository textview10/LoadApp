package com.instahela.deni.mkopo.util.security;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import kotlin.text.Charsets;

public class AESHelperJ {

    public static String encrypt(String key, String content) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] keyBytes = key.getBytes(Charsets.UTF_8);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(keyBytes, 0, keyBytes.length);
        byte[] keyData = md5.digest();
        SecretKeySpec sks = new SecretKeySpec(keyData, "AES");
        byte[] iv = new byte[]{ 0x00,
                0x01,
                0x02,
                0x03,
                0x04,
                0x05,
                0x06,
                0x07,
                0x08,
                0x09,
                0x0a,
                0x0b,
                0x0c,
                0x0d,
                0x0e,
                0x0f};

        IvParameterSpec paramSepc = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sks, paramSepc);

        byte[] plaintText = content.getBytes(Charsets.UTF_8);
        byte[] result = cipher.doFinal(plaintText);

        return Base64.encodeToString(result, Base64.NO_WRAP);

    }

}
