package router

import com.google.gson.Gson
import spark.Spark.path
import spark.Spark.get

val gson = Gson()

fun jsonTransformer(model: Any?): String {
    return gson.toJson(model)
}

fun init() {
    path("/api") {
        get("/ping", { req, res -> "pong" }, ::jsonTransformer)
    }
}