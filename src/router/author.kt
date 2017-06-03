package router

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import org.apache.ibatis.exceptions.PersistenceException
import spark.Request
import storage.Author
import storage.storageDAO
import token.TokenInfo
import token.tokenService

fun createAuthor(req: Request): Any? {
    val author = gson.fromJson(req.body(), Author::class.java)
    author.id = getUUID()
    author.createdAt = System.currentTimeMillis()
    author.key = getKey()
    author.secret = getUUID()
    author.initVector = getKey()

    try {
        storageDAO.createAuthor(mapOf(
            "id" to author.id,
            "name" to author.name,
            "created_at" to author.createdAt,
            "key" to author.key,
            "secret" to author.secret,
            "init_vector" to author.initVector
        ))
    } catch (e: PersistenceException) {
        if (e.cause is MySQLIntegrityConstraintViolationException) {
            throw APIException("duplicate author name")
        } else {
            throw e
        }
    }

    val tokenInfo = TokenInfo(
        author = author,
        count = 0
    )

    return tokenService?.generate(tokenInfo)
}
