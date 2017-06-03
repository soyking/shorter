import com.google.gson.Gson
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.junit.Before
import org.junit.Test
import router.CommonResponse


class APITest {
    val httpClient: HttpClient = DefaultHttpClient()
    val gson = Gson()

    fun parseResponse(response: HttpResponse): CommonResponse? {
        return gson.fromJson(EntityUtils.toString(response.getEntity()), CommonResponse::class.java)
    }

    fun post(url: String, entity: String): CommonResponse? {
        val post = HttpPost(url)
        post.entity = StringEntity(entity)
        post.setHeader("Content-type", "application/json")
        return parseResponse(httpClient.execute(post))
    }

    @Before
    fun clean() {
        MySQLStorageDAOImplTest.cleanDB()
    }


    @Test
    fun CreateAuthorTest() {
        val url = "http://localhost:8000/api/author"
        val authorName = "{\"name\": \"author1\"}"

        var resp = post(url, authorName)
        assert(resp!!.err == null)
        resp = post(url, authorName)
        assert(resp!!.err != null)
    }
}
