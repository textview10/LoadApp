package com.instahela.deni.mkopo.util.security.kotlin

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESHelper {

    fun encrypt(key: String, content: String): String {
        val keyBytes = key.toByteArray(Charsets.UTF_8)
        var md = MessageDigest.getInstance("MD5")
        md.update(keyBytes, 0, keyBytes.size)
        val keyData = md.digest()
        val sks = SecretKeySpec(keyData, "AES")
        val iv = byteArrayOf(
            0x00,
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
            0x0f
        )

        val paramSepc = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, sks, paramSepc)

        val plaintText = content.toByteArray(Charsets.UTF_8)
        val result = cipher.doFinal(plaintText)
        try {
            return Base64.encodeToString(result, Base64.NO_WRAP)
        } catch (error:OutOfMemoryError) {
            error.printStackTrace()
        }

        return ""
    }

//    fun decrypt(key: String, content: String): String {
//
//    }

}
