package router

import com.google.gson.Gson
import spark.Spark.path
import spark.Spark.get
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


    path("/api") {
        get("/ping", { req, res -> "pong" }, ::jsonTransformer)
    }
}