package router

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import org.apache.ibatis.exceptions.PersistenceException
import spark.Request
import storage.Sheet
import storage.SheetType
import storage.anyToInt
import storage.storageDAO
import token.tokenService

fun createSheet(req: Request): Any? {
    val token = req.headers("X-Token") ?: throw APIException("without token")
    val tokenInfo = tokenService?.extract(token) ?: throw APIException("invalid token")
    if (tokenService?.checkTokenInfo(tokenInfo) == false) {
        throw APIException("exceed max sheets limit")
    }

    val sheet = gson.fromJson(req.body(), Sheet::class.java)
    sheet.text ?: throw APIException("empty text")
    try {
        val type = SheetType.valueOf(sheet.type!!)
        if (type == SheetType.LINK && sheet.link == null) {
            throw APIException("without link")
        }
    } catch (e: java.lang.IllegalArgumentException) {
        throw APIException("invalid sheet type")
    }


    sheet.id = getUUID()
    sheet.createdAt = System.currentTimeMillis()
    sheet.author = tokenInfo.author.id
    sheet.token = token.trimEnd('=')

    try {
        storageDAO.createSheet(mapOf(
            "id" to sheet.id,
            "created_at" to sheet.createdAt,
            "author" to sheet.author,
            "type" to sheet.type,
            "text" to sheet.text,
            "link" to sheet.link,
            "token" to sheet.token
        ))
    } catch (e: PersistenceException) {
        if (e.cause is MySQLIntegrityConstraintViolationException) {
            throw APIException("token has been used")
        } else {
            throw e
        }
    }

    tokenInfo.createdAt = System.currentTimeMillis()
    tokenInfo.count += 1
    return tokenService?.regenTokenInfo(tokenInfo)
}

fun getSheets(req: Request): Any? {
    val requestMap = mapOf(
        "id" to req.queryParams("id"),
        "token" to req.queryParams("token"),
        "offset" to anyToInt(req.queryParams("offset"), 0),
        "count" to anyToInt(req.queryParams("count"), 10)
    )
    return storageDAO.getSheets(requestMap)
}
