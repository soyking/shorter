package router

import com.google.gson.Gson
import spark.Service
import spark.Spark.*
import storage.MySQLStorageDAOImpl
import java.util.*

val gson = Gson()

fun jsonTransformer(model: Any?): String {
    return gson.toJson(model)
}

fun init(props: Properties) {
    val storageDAOImpl = MySQLStorageDAOImpl(
            props.getProperty("db_url"),
            props.getProperty("db_user"),
            props.getProperty("db_password")
    )


    port(props.getProperty("port", Service.SPARK_DEFAULT_PORT.toString()).toInt())
    path("/api") {
        get("/ping", { req, res -> "pong" }, ::jsonTransformer)
    }
}