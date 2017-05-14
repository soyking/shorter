package router

import com.google.gson.Gson
import spark.*
import spark.Spark.*
import storage.MySQLStorageDAOImpl
import storage.StorageDAO
import java.util.*

val gson = Gson()
val jsonTransformer = ResponseTransformer { model ->
    gson.toJson(model)
}

fun init(props: Properties) {
    val storageDAOImpl = MySQLStorageDAOImpl(
        props.getProperty("db_url"),
        props.getProperty("db_user"),
        props.getProperty("db_password")
    )


    port(props.getProperty("port", Service.SPARK_DEFAULT_PORT.toString()).toInt())
    path("/api") {
        get("/sheet", handle(storageDAOImpl, ::getSheets), jsonTransformer)
        post("/sheet", handle(storageDAOImpl, ::createSheet), jsonTransformer)
    }
}

class CommonResponse(var err: String = "", var data: Any? = null)

fun handle(storageDAO: StorageDAO, handler: (Request, StorageDAO) -> Any): Route {
    return Route { req, res ->
        var result: Any? = null
        try {
            result = handler(req, storageDAO)
        } catch (e: Exception) {
            println(e)
            return@Route CommonResponse("internal-error")
        }

        if (result is String) {
            CommonResponse(result)
        } else {
            CommonResponse("success", result)
        }
    }
}