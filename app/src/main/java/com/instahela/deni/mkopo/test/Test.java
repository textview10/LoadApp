package com.instahela.deni.mkopo.test;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author : xu.wang
 * Date   : 2021/10/26 5:48 下午
 * Desc   :
 */
public class Test {

    private final byte[] iv = new byte[]{0x00, 0x01, 0x02, 0x03, 0x4, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

    // 这个需要填充的情况
    private SecretKeySpec keySpec;
    private IvParameterSpec ivParameterSpec;

    private Cipher cipher;

    public void init(String privateKey) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(privateKey.getBytes(StandardCharsets.UTF_8));
            keySpec = new SecretKeySpec(md5.digest(), "AES");
            ivParameterSpec = new IvParameterSpec(iv);
            // 加密
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptMessage(String message) {
        // 对数据进行加密
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            byte[] doFinal = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(doFinal, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public String decryptMessage(String ciphertext) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            byte[] decode = new byte[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decode = Base64.decode(ciphertext, Base64.DEFAULT);
                byte[] doFinal = cipher.doFinal(decode);
                return new String(doFinal);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void main() {
        init("rp9v9muvsmh1nsd8");
        String c = "/etJETrXM/lwjayD+xkNGg==";
        String s = decryptMessage(c);
        Log.e("Test", "" + s);
        System.out.println(s);
    }
}
