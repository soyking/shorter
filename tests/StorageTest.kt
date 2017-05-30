import org.junit.After
import org.junit.Before
import org.junit.Test
import storage.*


abstract class AbstractStorageDAOImplTest {
    var impl: StorageDAO? = null

    val authorName = "authorName"
    val authorKey = "authorKey"
    val authorSecret = "authorSecret"

    val sheetType = SheetType.TEXT
    val sheetText = "sheetText"
    val sheetLink = "sheetLink"

    abstract fun connect()
    abstract fun clear()

    fun _createAuthor(): String {
        val id = getUUID()
        impl!!.createAuthor(
            mapOf(
                "id" to id,
                "name" to authorName,
                "created_at" to System.currentTimeMillis(),
                "key" to authorKey,
                "secret" to authorSecret
            )
        )
        return id
    }

    @Test
    fun getAuthor() {
        // not exist
        assert(impl!!.getAuthor("not_exist") == null)

        val authorID = _createAuthor()
        val author = impl!!.getAuthor(authorID)
        assert(author!!.name == authorName)
        assert(author.key == authorKey)
        assert(author.secret == authorSecret)
    }

    fun _createSheet(authorID: String): Pair<String, String> {
        val id = getUUID()
        val token = getUUID()
        impl!!.createSheet(
            mapOf(
                "id" to id,
                "created_at" to System.currentTimeMillis(),
                "author" to authorID,
                "type" to sheetType.toString(),
                "text" to sheetText,
                "link" to sheetLink,
                "token" to token
            )
        )
        return Pair(id, token)
    }

    @Test
    fun getSheets() {
        assert(impl!!.getSheets(mapOf("id" to "not_exist"))!!.isEmpty())
        assert(impl!!.getSheets(mapOf("token" to "not_exist"))!!.isEmpty())

        val authorID = _createAuthor()
        val sheetList = ArrayList<Pair<String, String>>()
        for (i in 1..10) {
            sheetList.add(_createSheet(authorID))
        }

        assert(impl!!.getSheets(mapOf("id" to sheetList.first().first))!!.size == 1)
        assert(impl!!.getSheets(mapOf("token" to sheetList.first().second))!!.size == 1)
        assert(impl!!.getSheets(mapOf("offset" to 0, "count" to 4))!!.size == 4)

        val sheets = impl!!.getSheets(mapOf("offset" to 5, "count" to 5))
        assert(sheets!!.size == 5)
        // order by created_at desc
        assert(sheets.last().id == sheetList.first().first)
    }
}

open class MySQLStorageDAOImplTest : AbstractStorageDAOImplTest() {
    var _impl: MySQLStorageDAOImpl? = null

    @Before
    override fun connect() {
        _impl = MySQLStorageDAOImpl("jdbc:mysql://127.0.0.1:3306/shorter", "shorter", "shorter")
        impl = _impl
    }

    @After
    override fun clear() {
        for (tb in arrayOf(_impl!!.TABLE_SHEET, _impl!!.TABLE_AUTHOR)) {
            val stmt = _impl!!.connection.createStatement()
            val query = "delete from " + tb
            stmt.executeUpdate(query)
        }
    }
}

class MybatisStorageDAOImplTest : MySQLStorageDAOImplTest() {
    @Before
    override fun connect() {
        // no raw sql execution, shame on mybatis
        super.connect()
        impl = MyBatisStorageDAOImpl("mybatis/mybatis.xml")
    }
}
