package router

import storage.Author
import storage.storageDAO

interface Assembler {
    fun combine(parts: List<String?>): String
    fun extract(text: String): List<String>
}

class SplitAssembler : Assembler {
    val SEPARATOR = "|"

    override fun combine(parts: List<String?>): String {
        return parts.joinToString(separator = SEPARATOR)
    }

    override fun extract(text: String): List<String> {
        return text.split(delimiters = SEPARATOR)
    }
}

interface Cipher {
    fun encrypt(plainText: String): String
    fun decrypt(cipherText: String): String
}

interface CipherFactory {
    fun create(key: String): Cipher
}

class TokenInfo(val author: Author, val createdAt: Long, val count: Int)

class TokenService(val key: String, val cipherFactory: CipherFactory, val assembler: Assembler) {
    private val FIRST_CLASS_CONTENT_LENGTH = 2
    private val SECOND_CLASS_CONTENT_LENGTH = 3
    private val firstClassCipher = cipherFactory.create(key)

    fun generate(tokenInfo: TokenInfo): String? {
        val author = tokenInfo.author
        val secondClassCipher = cipherFactory.create(author.key!!)

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
        if (parts.size != FIRST_CLASS_CONTENT_LENGTH) {
            return null
        }

        val authorID = parts[0]
        val subToken = parts[1]

        val author = storageDAO.getAuthor(authorID) ?: return null
        val secondClassCipher = cipherFactory.create(author.key!!)

        val subParts = assembler.extract(secondClassCipher.decrypt(subToken))
        if (subParts.size != SECOND_CLASS_CONTENT_LENGTH) {
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
}
