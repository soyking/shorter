import org.junit.Test
import token.AESCipher

class CipherTest {
    @Test
    fun EncryptAndDecrypt() {
        val key = "Bar12345Bar12345"
        val initVector = "RandomInitVector"

        val cipher = AESCipher(key, initVector)
        val plaintext = "what now???"
        val ciphertext = cipher.encrypt(plaintext)
        println("cipher text: " + ciphertext)
        val _plaintext = cipher.decrypt(ciphertext!!)
        println("plain text: " + _plaintext)
        assert(plaintext == _plaintext)
    }
}
