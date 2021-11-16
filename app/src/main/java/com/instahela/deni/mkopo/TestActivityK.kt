package com.instahela.deni.mkopo

import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.EncryptUtils
import com.instahela.deni.mkopo.test.Test2
import com.instahela.deni.mkopo.util.security.RSAHelperJ
import com.instahela.deni.mkopo.util.security.StringUtilsJ
import com.instahela.deni.mkopo.util.security.kotlin.AESKeyGenerator
import com.instahela.deni.mkopo.util.security.kotlin.RSAHelper
import com.instahela.deni.mkopo.util.security.kotlin.aes
import com.instahela.deni.mkopo.util.security.kotlin.base64

class TestActivityK : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1)

//        test();
        Test2.createKeyPair()
        test1()
    }


    private fun test1() {
        val aesKey : String = "rp9v9muvsmh1nsd8";
//        val  str1 : String =  Test2.encryptWithRSA(aesKey);
//        Log.e("Test", str1 );
//        val result2 = Test2.decryptWithRSA(str1)
//        Log.e("Test", result2 );

        val  str1 : String = RSAHelperJ.encryptByRSA(aesKey);
        Log.e("Test", str1 );
        val result2 = RSAHelperJ.decryptByRSA(str1)
        Log.e("Test", result2 );

//        val  str2 : String = RSAHelperJ.encryptByRSA(aesKey)
//        val  str3 : String = RSAHelper.encryptByRSA(aesKey)
//        val  str4 : String = RSAHelperJ.encryptByRSA(aesKey)
//        val isEqual : Boolean = TextUtils.equals(str1, str2)
//        Log.e("Test", isEqual.toString() );

//        Log.e("Test", str2);
//        Log.e("Test", str3);
//        Log.e("Test", str4);
//
//
//
//        val result2 = RSAHelperJ.decryptByRSA(str2)
//        val result3 = RSAHelperJ.decryptByRSA(str3)
//        val result4 = RSAHelperJ.decryptByRSA(str4)
//        Log.e("Test", " result ---------------")
//        Log.e("Test", result2.toString() );
//        Log.e("Test", result3.toString() );
//        Log.e("Test", result4.toString() );
//        Log.e("Test", " result 2 ---------------")
//        val str = AESKeyGenerator.instance.getAesEncrypedKey;
//        Log.e("Test", str)
    }

    private fun test() {
        var str : String = "11111111111111111";
        val aes1 = StringUtilsJ.aes(str);
        val  aes2 = str.aes();
        Log.e("TEst1", aes1)
        Log.e("TEst2 ", aes2)


       val uploadValueStr : String =  str.aes().base64();
    }
}