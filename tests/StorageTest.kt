import org.junit.After
import org.junit.Before
import org.junit.Test
import storage.*


class MySQLStorageDAOImplTest {
    var impl: MySQLStorageDAOImpl? = null

    val authorName = "authorName"
    val authorKey = "authorKey"
    val authorSecret = "authorSecret"

    val sheetType = SheetType.TEXT
    val sheetText = "sheetText"
    val sheetLink = "sheetLink"

    @Before
    fun connect() {
        impl = MySQLStorageDAOImpl("jdbc:mysql://127.0.0.1:3306/shorter", "shorter", "shorter")
    }

    @After
    fun clear() {
        val connection = impl!!.connection
        for (tb in arrayOf(impl!!.TABLE_SHEET, impl!!.TABLE_AUTHOR)) {
            val stmt = connection.createStatement()
            val query = "delete from " + tb
            stmt.executeUpdate(query)
        }
    }

    fun _createAuthor(): String {
        val id = getUUID()
        impl!!.createAuthor(
            id = id,
            name = authorName,
            createdAt = System.currentTimeMillis(),
            key = authorKey,
            secret = authorSecret
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

    fun _createSheet(authorID: String): String {
        val id = getUUID()
        impl!!.createSheet(
            id = id,
            createdAt = System.currentTimeMillis(),
            author = authorID,
            type = sheetType,
            text = sheetText,
            link = sheetLink
        )
        return id
    }

    @Test
    fun getSheets() {
        assert(impl!!.getSheets(mapOf("id" to "not_exist"))!!.isEmpty())

        val authorID = _createAuthor()
        val sheetList = ArrayList<String>()
        for (i in 1..10) {
            sheetList.add(_createSheet(authorID))
        }

        assert(impl!!.getSheets(mapOf("id" to sheetList.first()))!!.size == 1)
        assert(impl!!.getSheets(mapOf("pages" to "1", "count" to "4"))!!.size == 4)

        val sheets = impl!!.getSheets(mapOf("pages" to "2", "count" to "5"))
        assert(sheets!!.size == 5)
        // order by created_at desc
        assert(sheets.last().id == sheetList.first())
    }
}

class MybatisStorageDAOImplTest {
    var impl: MyBatisStorageDAOImpl? = null

    @Before
    fun connect() {
        impl = MyBatisStorageDAOImpl("mybatis/mybatis.xml")
    }

    @After
    fun clear() {

    }

    @Test
    fun getAuthor() {
        // not exist
        assert(impl!!.getAuthor("not_exist") == null)

        val author_ = impl!!.getAuthor("test")
        println(author_!!.key)
    }
}
