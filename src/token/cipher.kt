package token

import java.lang.Exception
import java.util.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher as javaCipher

interface Cipher {
    fun encrypt(plainText: String): String?
    fun decrypt(cipherText: String): String?
}

interface CipherFactory {
    fun create(key: String): Cipher
}

/**
 * key: 128 bit
 * initVector: 128 bit
 */
class AESCipher(key: String, initVector: String) : Cipher {
    private val encryptCipher: javaCipher
    private val decryptCipher: javaCipher

    init {
        val iv: IvParameterSpec = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
        val secretKey: SecretKeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
        encryptCipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5PADDING")
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey, iv)
        decryptCipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5PADDING")
        decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey, iv)
    }

    override fun encrypt(plainText: String): String? {
        try {
            return String(Base64.getEncoder().encode(encryptCipher.doFinal(plainText.toByteArray())))
        } catch (e: Exception) {
            return null
        }
    }

    override fun decrypt(cipherText: String): String? {
        try {
            return String(decryptCipher.doFinal(Base64.getDecoder().decode(cipherText)))
        } catch (e: Exception) {
            return null
        }
    }
}
