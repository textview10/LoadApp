package com.instahela.deni.mkopo.util.security.kotlin

import android.util.Base64
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSAHelper {

    val RSA_PUBLISH_KEY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0WRjHz4MPCiLYtR8WTqBM/pqtLWtJbJDHMZTFa2riljAxIh86+YuG0fB341yK8Zn/sZBGQU4EsdwWHeqg+2z3xodGm8AiAMcWF2qdOX1SB6JTT5+yGgXQT1WF5OjN3qoJ2GemFDWKqXvagY6SqRYN+hTZ8p/VfWa/1ydh5/E2JwIDAQAB"

    fun getPublicKey(key: String): PublicKey {
        val keyBytes = Base64.decode(key, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }


    fun getPrivateKey(key: String): PrivateKey {
        val keyBytes = Base64.decode(key, Base64.DEFAULT)
        val keySPec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySPec)
    }


    fun encryptByRSA(content: String): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        val plainTextBytes = content.toByteArray()
        val publicKey = getPublicKey(RSA_PUBLISH_KEY)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptBytes = cipher.doFinal(plainTextBytes)
        return Base64.encodeToString(encryptBytes, Base64.NO_WRAP)
    }

}