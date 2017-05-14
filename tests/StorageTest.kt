import org.junit.After
import org.junit.Before
import org.junit.Test
import storage.Author
import storage.MySQLStorageDAOImpl
import storage.Sheet
import storage.SheetType


class MySQLStorageDAOImplTest {
    var impl: MySQLStorageDAOImpl? = null

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

    fun _createAuthor(): Pair<MutableMap<String, Any>, Author?> {
        val args = mutableMapOf<String, Any>("name" to "test_author")
        val author = impl!!.createAuthor(args)
        return Pair(args, author)
    }

    @Test
    fun createAuthor() {
        val result = _createAuthor()
        val args = result.first
        assert(result.second != null)

        // invalid name
        args["name"] = 1
        assert(impl!!.createAuthor(args) == null)

        // without name
        args.remove("name")
        assert(impl!!.createAuthor(args) == null)
    }

    @Test
    fun getAuthor() {
        // not exist
        assert(impl!!.getAuthor("not_exist") == null)

        val author = impl!!.createAuthor(mapOf("name" to "test_author"))
        val author_ = impl!!.getAuthor(author!!.id)
        assert(author.name == author_!!.name)
        assert(author.createdAt == author_.createdAt)
        assert(author.key == author_.key)
    }

    fun _createSheet(author: Author?): Pair<MutableMap<String, Any>, Sheet?> {
        val args = mutableMapOf<String, Any>(
            "author" to author!!.id,
            "type" to SheetType.TEXT.toString(),
            "text" to "some_text"
        )
        val sheet = impl!!.createSheet(args)
        return Pair(args, sheet)
    }

    @Test
    fun createSheet() {
        val author = _createAuthor().second
        val result = _createSheet(author)
        val args = result.first

        assert(result.second != null)

        // no link
        args["type"] = SheetType.LINK.toString()
        assert(impl!!.createSheet(args) == null)
        args["link"] = "http://test.com"
        assert(impl!!.createSheet(args) != null)

        args["type"] = "not_exist_type"
        assert(impl!!.createSheet(args) == null) // invalid type
    }

    @Test
    fun getSheets() {
        assert(impl!!.getSheets(mapOf("id" to "not_exist"))!!.isEmpty())

        val sheetList = ArrayList<Sheet>()
        val author = _createAuthor().second
        for (i in 1..10) {
            sheetList.add(_createSheet(author).second!!)
        }

        assert(impl!!.getSheets(mapOf("id" to sheetList.first().id))!!.size == 1)
        assert(impl!!.getSheets(mapOf("pages" to 1, "count" to 4))!!.size == 4)

        val sheets = impl!!.getSheets(mapOf("pages" to 2, "count" to 5))
        assert(sheets!!.size == 5)
        // order by created_at desc
        assert(sheets.last().id == sheetList.first().id)
    }
}
