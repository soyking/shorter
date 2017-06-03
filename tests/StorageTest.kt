import org.junit.Before
import org.junit.Test
import router.getKey
import router.getUUID
import storage.MyBatisStorageDAOImpl
import storage.MySQLStorageDAOImpl
import storage.SheetType
import storage.StorageDAO


abstract class AbstractStorageDAOImplTest {
    var impl: StorageDAO? = null

    val authorName = "authorName"
    val authorKey = getKey()
    val authorSecret = "authorSecret"
    val authorInitVector = getKey()

    val sheetType = SheetType.TEXT
    val sheetText = "sheetText"
    val sheetLink = "sheetLink"

    abstract fun connect()

    fun _createAuthor(): String {
        impl!!.createAuthor(
            mapOf(
                "name" to authorName,
                "created_at" to System.currentTimeMillis(),
                "key" to authorKey,
                "secret" to authorSecret,
                "init_vector" to authorInitVector
            )
        )
        return authorName
    }

    @Test
    fun getAuthor() {
        // not exist
        assert(impl!!.getAuthor("not_exist") == null)

        val authorName = _createAuthor()
        val author = impl!!.getAuthor(authorName)
        assert(author!!.name == authorName)
        assert(author.key == authorKey)
        assert(author.secret == authorSecret)
        println(author.initVector)
        assert(author.initVector == authorInitVector)
    }

    fun _createSheet(authorName: String): Pair<String, String> {
        val id = getUUID()
        val token = getUUID()
        impl!!.createSheet(
            mapOf(
                "id" to id,
                "created_at" to System.currentTimeMillis(),
                "author" to authorName,
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

        val authorName = _createAuthor()
        val sheetList = ArrayList<Pair<String, String>>()
        for (i in 1..10) {
            sheetList.add(_createSheet(authorName))
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
    companion object {
        fun cleanDB(): StorageDAO {
            val _impl = MySQLStorageDAOImpl("jdbc:mysql://127.0.0.1:3306/shorter", "shorter", "shorter")
            for (tb in arrayOf(_impl.TABLE_SHEET, _impl.TABLE_AUTHOR)) {
                val stmt = _impl.connection.createStatement()
                val query = "delete from " + tb
                stmt.executeUpdate(query)
            }
            return _impl
        }
    }

    @Before
    override fun connect() {
        impl = cleanDB()
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
