package com.instahela.deni.mkopo.util.security;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.instahela.deni.mkopo.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import kotlin.text.Charsets;

public class StringUtilsJ {

    public static String getUrlEncode(String url){
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String aes(String contentStr) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String aesKey = AESKeyGeneratorJ.getInstance().getAESKey();
        if (TextUtils.isEmpty(contentStr)) {
            return "";
        }
        String content =  AESHelperJ.encrypt(aesKey, contentStr);
        if(BuildConfig.DEBUG) {
            Log.d("aes","aeskey:${aesKey}\ncontent=${content}");
        }
        return content;
    }

    public static String base64(String contentStr){
        byte[] data = contentStr.getBytes(Charsets.UTF_8);
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static boolean isKenyaPhoneNumber(String contentStr){
        String str = contentStr.replace("+", "");
        if (str.startsWith("0")) {
            return str.length() == 10;
        }

        if (str.startsWith("254")) {
            return str.length() == 12;
        }
        return str.length() == 9;
    }

}
