package com.instahela.deni.mkopo.util.security;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAHelperJ {

    public static final String RSA_PUBLISH_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0WRjHz4MPCiLYtR8WTqBM/pqtLWtJbJDHMZTFa2riljAxIh86+YuG0fB341yK8Zn/sZBGQU4EsdwWHeqg+2z3xodGm8AiAMcWF2qdOX1SB6JTT5+yGgXQT1WF5OjN3qoJ2GemFDWKqXvagY6SqRYN+hTZ8p/VfWa/1ydh5/E2JwIDAQAB";

      public static PublicKey getPublicKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
          byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
          X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          return keyFactory.generatePublic(keySpec);
      }

    public static PrivateKey getPrivateKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static String encryptByRSA(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] plainTextBytes = content.getBytes();
        PublicKey publicKey = getPublicKey(RSA_PUBLISH_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptBytes = cipher.doFinal(plainTextBytes);
        return Base64.encodeToString(encryptBytes, Base64.NO_WRAP);
    }


    public static String decryptByRSA(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
          Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] plainTextBytes = content.getBytes();
        byte[] decode = Base64.decode(plainTextBytes, Base64.NO_WRAP);
        PrivateKey privateKey = getPrivateKey(RSA_PUBLISH_KEY);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptBytes = cipher.doFinal(decode);
        return new String(encryptBytes);
    }
}
