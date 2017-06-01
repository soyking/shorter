package router

import spark.Request
import storage.anyToInt
import storage.storageDAO

fun createSheet(req: Request): Any? {
    return "done"
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
