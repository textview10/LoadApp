package com.instahela.deni.mkopo.util.security.kotlin

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.instahela.deni.mkopo.BuildConfig
import java.net.URLEncoder


fun String.urlencode(): String {
    if (TextUtils.isEmpty(this)) {
        return ""
    }
    return URLEncoder.encode(this, "UTF-8")
}

fun String.aes(): String {
    val aesKey = AESKeyGenerator.instance.sAESKey
    if (TextUtils.isEmpty(this)) {
        return ""
    }
    val content =  AESHelper.encrypt(aesKey, this)
    if(BuildConfig.DEBUG) {
        Log.d("aes","aeskey:${aesKey}\ncontent=${content}")
    }
    return content
}

fun String.base64(): String {
    val data: ByteArray = this.toByteArray(Charsets.UTF_8)
    return Base64.encodeToString(data, Base64.DEFAULT);
}

fun String.isKenyaPhoneNumber(): Boolean {
    var str = this.replace("+", "")
    if (str.startsWith("0")) {
        return str.length == 10
    }

    if (str.startsWith("254")) {
        return str.length == 12
    }
    return str.length == 9
}

