import org.junit.After
import org.junit.Before
import org.junit.Test
import storage.MySQLStorageDAOImpl
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

    @Test
    fun createAuthor() {
        val args = mutableMapOf<String, Any>("name" to "test_author")
        assert(impl!!.createAuthor(args) != null)
        args["name"] = 1
        assert(impl!!.createAuthor(args) == null)
        args.remove("name")
        assert(impl!!.createAuthor(args) == null)
    }

    @Test
    fun getAuthor() {
        assert(impl!!.getAuthor("not_exist") == null) // not exist

        val author = impl!!.createAuthor(mapOf("name" to "test_author"))
        val author_ = impl!!.getAuthor(author!!.id)
        assert(author.name == author_!!.name)
        assert(author.createdAt == author_.createdAt)
        assert(author.key == author_.key)
    }

    @Test
    fun createSheet() {
        val author = impl!!.createAuthor(mapOf("name" to "test_author"))!!
        val args = mutableMapOf<String, Any>(
                "author" to author.id,
                "type" to SheetType.TEXT.toString(),
                "text" to "some_text"
        )
        assert(impl!!.createSheet(args) != null)

        args["type"] = SheetType.LINK.toString()
        assert(impl!!.createSheet(args) == null) // no link
        args["link"] = "http://test.com"
        assert(impl!!.createSheet(args) != null)

        args["type"] = "not_exist_type"
        assert(impl!!.createSheet(args) == null) // invalid type
    }
}
