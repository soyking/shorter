import org.junit.Test
import storage.Author
import storage.Sheet
import storage.StorageDAO
import storage.storageDAO
import token.AESCipher
import token.SplitAssembler
import token.TokenInfo
import token.TokenService

class CipherTest {
    val key = "Bar12345Bar12345"
    val initVector = "RandomInitVector"

    @Test
    fun CipherTest() {
        val cipher = AESCipher(key, initVector)
        val plaintext = "what now???"
        val ciphertext = cipher.encrypt(plaintext)
        println("cipher text: " + ciphertext)
        val _plaintext = cipher.decrypt(ciphertext)
        println("plain text: " + _plaintext)
        assert(plaintext == _plaintext)
    }

    @Test
    fun TokenServiceTest() {
        val service = TokenService(
            key = key,
            initVector = initVector,
            cipherFactory = AESCipher,
            assembler = SplitAssembler(),
            maxSheets = 3
        )

        val author = Author(
            id = "id",
            name = "test_name",
            createdAt = System.currentTimeMillis(),
            key = key,
            secret = "secrect_key"
        )
        // dirty patch
        storageDAO = object : StorageDAO {
            override fun createAuthor(params: Map<String, Any?>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAuthor(id: String): Author? {
                return author
            }

            override fun createSheet(params: Map<String, Any?>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getSheets(params: Map<String, Any>): ArrayList<Sheet>? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        val tokenInfo = TokenInfo(author, System.currentTimeMillis(), 1)
        val token = service.generate(tokenInfo)!!
        println("token: " + token)
        val _tokenInfo = service.extract(token)
        assert(tokenInfo.count == _tokenInfo!!.count)

        assert(service.checkTokenInfo(tokenInfo))
        tokenInfo.count = 3
        assert(!service.checkTokenInfo(tokenInfo))
        tokenInfo.createdAt -= 60000 * 60 * 24
        assert(service.checkTokenInfo(tokenInfo))
    }
}
