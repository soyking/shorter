import com.google.gson.Gson
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.junit.Before
import org.junit.Test
import router.*
import storage.Author
import storage.Sheet
import storage.SheetType


class APITest {
    val host = "http://localhost:8000"
    val httpClient: HttpClient = DefaultHttpClient()
    val gson = Gson()

    fun parseResponse(response: HttpResponse): CommonResponse? {
        return gson.fromJson(EntityUtils.toString(response.entity), CommonResponse::class.java)
    }

    fun post(url: String, entity: Any, token: String = ""): CommonResponse? {
        val post = HttpPost(url)
        post.addHeader("X-Token", token)
        post.entity = StringEntity(gson.toJson(entity))
        post.setHeader("Content-type", "application/json")
        return parseResponse(httpClient.execute(post))
    }

    @Before
    fun clean() {
        MySQLStorageDAOImplTest.cleanDB()
    }

    fun _createAuthor(): CommonResponse? {
        val url = host + "/api/author"
        val author = Author(name = "author1")

        return post(url, author)
    }

    @Test
    fun CreateAuthorTest() {
        var resp = _createAuthor()
        assert(resp!!.err == null)
        resp = _createAuthor()
        assert(resp!!.err == DuplicateAuthorErr.err)
    }

    @Test
    fun CreateSheetTest() {
        val url = host + "/api/sheet"
        val sheet = Sheet(type = "not_exist")

        var resp = post(url, sheet)
        assert(resp!!.err == WithoutTokenErr.err)

        val token = _createAuthor()?.data as String
        resp = post(url, sheet, token + "_invalid_part")
        assert(resp!!.err == InvalidTokenErr.err)

        resp = post(url, sheet, token)
        assert(resp!!.err == EmptyTextErr.err)

        sheet.text = "text"
        resp = post(url, sheet, token)
        assert(resp!!.err == InvalidSheetTypeErr.err)

        sheet.type = SheetType.LINK.toString()
        resp = post(url, sheet, token)
        assert(resp!!.err == WithoutLinkErr.err)

        sheet.link = "link"
        resp = post(url, sheet, token)
        assert(resp!!.err == null)
        var nextToken = resp.data as String

        resp = post(url, sheet, token)
        assert(resp!!.err == ReuseTokenErr.err)

        val maxSheets = 3
        for (i in 0..(maxSheets - 2)) {
            resp = post(url, sheet, nextToken)
            assert(resp!!.err == null)
            nextToken = resp.data as String
        }

        resp = post(url, sheet, nextToken)
        assert(resp!!.err == ExceedMaxSheetsLimitErr.err)
    }
}
