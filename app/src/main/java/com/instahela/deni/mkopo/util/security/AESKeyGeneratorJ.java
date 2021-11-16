package com.instahela.deni.mkopo.util.security;

import android.util.Log;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AESKeyGeneratorJ {

    private volatile static AESKeyGeneratorJ mInstance;

    private String sAESKey;

    private String encryptedKey;

    private AESKeyGeneratorJ() {
        sAESKey = generateRandom128Key();
        try {
            encryptedKey  = RSAHelperJ.encryptByRSA(sAESKey);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

    public static AESKeyGeneratorJ getInstance() {
        if (mInstance == null) {
            synchronized (AESKeyGeneratorJ.class) {
                if (mInstance == null) {
                    mInstance = new AESKeyGeneratorJ();
                }
            }
        }
        return mInstance;
    }

    private static String generateRandom128Key() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32).substring(0, 16);
    }

    public String getAESKey(){
        return sAESKey;
    }

    public String getEncryptedKey(){
        return encryptedKey;
    }
}
