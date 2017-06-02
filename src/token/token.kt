package token

import storage.Author
import storage.storageDAO
import java.util.*

class TokenInfo(val author: Author, var createdAt: Long = 0, var count: Int)

class TokenService(key: String, val initVector: String,
                   val cipherFactory: CipherFactory, val assembler: Assembler,
                   val maxSheets: Int) {
    private val FIRST_CLASS_CONTENT_LENGTH = 2
    private val SECOND_CLASS_CONTENT_LENGTH = 3
    private val firstClassCipher = cipherFactory.create(key, initVector)

    fun generate(tokenInfo: TokenInfo): String? {
        val author = tokenInfo.author
        val secondClassCipher = cipherFactory.create(author.key!!, initVector)

        val subToken = secondClassCipher.encrypt(
            assembler.combine(listOf(
                author.secret,
                System.currentTimeMillis().toString(),
                tokenInfo.count.toString()
            ))
        )
        val token = firstClassCipher.encrypt(
            assembler.combine(listOf(
                author.id,
                subToken
            ))
        )

        return token
    }

    fun extract(token: String): TokenInfo? {
        val parts = assembler.extract(firstClassCipher.decrypt(token))
        if (parts == null || parts.size != FIRST_CLASS_CONTENT_LENGTH) {
            return null
        }

        val authorID = parts[0]
        val subToken = parts[1]

        val author = storageDAO.getAuthor(authorID) ?: return null
        val secondClassCipher = cipherFactory.create(author.key!!, initVector)

        val subParts = assembler.extract(secondClassCipher.decrypt(subToken))
        if (subParts == null || subParts.size != SECOND_CLASS_CONTENT_LENGTH) {
            return null
        }

        val secret = subParts[0]
        if (secret != author.secret) {
            return null
        }

        val createdAt = subParts[1].toLong()
        val count = subParts[2].toInt()

        return TokenInfo(author = author, createdAt = createdAt, count = count)
    }

    private fun isCreatedInToday(createdAt: Long): Boolean {
        val now = Calendar.getInstance()
        val created = Calendar.getInstance()
        created.timeInMillis = createdAt

        return (now.get(Calendar.YEAR) == created.get(Calendar.YEAR)
            && now.get(Calendar.DAY_OF_YEAR) == created.get(Calendar.DAY_OF_YEAR))
    }

    // return true when not exceed max number of sheets
    fun checkTokenInfo(tokenInfo: TokenInfo): Boolean {
        return (!(isCreatedInToday(tokenInfo.createdAt) && tokenInfo.count >= maxSheets))
    }

    fun regenTokenInfo(tokenInfo: TokenInfo): String? {
        if (isCreatedInToday(tokenInfo.createdAt)) {
            tokenInfo.count += 1
        } else {
            tokenInfo.count = 0
        }
        tokenInfo.createdAt = System.currentTimeMillis()
        return generate(tokenInfo)
    }
}

var tokenService: TokenService? = null

fun init(key: String, initVector: String, _maxSheets: Any?) {
    val maxSheets = _maxSheets as? Int ?: 3
    println(maxSheets)

    tokenService = TokenService(
        key = key,
        initVector = initVector,
        cipherFactory = AESCipher,
        assembler = SplitAssembler(),
        maxSheets = maxSheets
    )
}
