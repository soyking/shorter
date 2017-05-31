package router

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import org.apache.ibatis.exceptions.PersistenceException
import spark.Request
import storage.storageDAO

fun createAuthor(req: Request): Any? {
    val requestMap = gson.fromJson(req.body(), HashMap<String, Any>().javaClass)
    requestMap["id"] = getUUID()
    requestMap["created_at"] = System.currentTimeMillis()
    requestMap["key"] = getKey()
    requestMap["secret"] = getUUID()

    try {
        storageDAO.createAuthor(requestMap)
    } catch (e: PersistenceException) {
        if (e.cause is MySQLIntegrityConstraintViolationException) {
            return "duplicate author name"
        } else {
            throw e
        }
    }

    return null
}
