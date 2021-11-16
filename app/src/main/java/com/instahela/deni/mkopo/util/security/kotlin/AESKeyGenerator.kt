package com.instahela.deni.mkopo.util.security.kotlin

import java.math.BigInteger
import java.security.SecureRandom

class AESKeyGenerator private constructor() {


    val sAESKey: String by lazy {
        val key = generateRandom128Key()
        key
    }

    val getAesEncrypedKey: String by lazy {
        val encryptedKey = RSAHelper.encryptByRSA(sAESKey)
        encryptedKey
    }

    companion object {
        val instance: AESKeyGenerator by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AESKeyGenerator()
        }

        fun generateRandom128Key(): String {
            val random = SecureRandom()
            return BigInteger(130, random).toString(32).substring(0, 16)
        }
    }
}