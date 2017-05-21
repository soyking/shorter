package router

import spark.Request
import storage.StorageDAO


fun createSheet(req: Request, storage: StorageDAO): Any {
    return "done"
}

fun getSheets(req: Request, storage: StorageDAO): Any {
    return "done"
}
