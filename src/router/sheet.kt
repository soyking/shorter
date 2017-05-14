package router

import spark.Request
import storage.StorageDAO


fun createSheet(req: Request, storage: StorageDAO): Any {
    val requestMap = gson.fromJson(req.body(), HashMap<String, Any>().javaClass)
    val sheet = storage.createSheet(requestMap)
    if (sheet == null) return "create error" else return sheet
}

fun getSheets(req: Request, storage: StorageDAO): Any {
    val requestMap = mapOf(
        "id" to req.queryParams("id"),
        "pages" to req.queryParams("pages"),
        "count" to req.queryParams("count")
    )
    return storage.getSheets(requestMap) ?: "fetch error"
}
