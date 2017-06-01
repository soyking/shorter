package router

import com.google.gson.Gson
import spark.Request
import spark.ResponseTransformer
import spark.Route
import spark.Service
import spark.Spark.*
import java.util.*

val gson = Gson()
val jsonTransformer = ResponseTransformer { model ->
    gson.toJson(model)
}

fun init(props: Properties) {
    port(props.getProperty("port", Service.SPARK_DEFAULT_PORT.toString()).toInt())
    token.init(props.getProperty("key"), props.getProperty("init_vector"))
    path("/api") {
        post("/author", handle(::createAuthor), jsonTransformer)

        get("/sheet", handle(::getSheets), jsonTransformer)
        post("/sheet", handle(::createSheet), jsonTransformer)
    }
}

class APIException(val err: String) : Exception(err)

data class CommonResponse(var err: String? = null, var data: Any? = null)

fun handle(handler: (Request) -> Any?): Route {
    return Route { req, _ ->
        try {
            CommonResponse(data = handler(req))
        } catch (e: APIException) {
            return@Route CommonResponse(e.err)
        } catch (e: Exception) {
            println(e)
            return@Route CommonResponse("internal-error")
        }
    }
}