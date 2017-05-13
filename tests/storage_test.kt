import org.junit.After
import org.junit.Before
import org.junit.Test
import storage.MySQLStorageDAOImpl


class MySQLStorageDAOImplTest {
    var impl: MySQLStorageDAOImpl? = null

    @Before
    fun connect() {
        impl = MySQLStorageDAOImpl("jdbc:mysql://127.0.0.1:3306/shorter", "shorter", "shorter")
    }

    @After
    fun clear() {
        val connection = impl!!.connection
        for (tb in arrayOf(impl!!.TABLE_AUTHOR, impl!!.TABLE_SHEET)) {
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

}