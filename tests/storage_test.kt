import org.junit.Before
import org.junit.Test
import storage.MySQLStorageDAOImpl


class MySQLStorageDAOImplTest {
    var impl: MySQLStorageDAOImpl? = null

    @Before
    fun setUp() {
        impl = MySQLStorageDAOImpl("jdbc:mysql://127.0.0.1:3306/shorter", "shorter", "shorter")
    }

    @Test
    fun createAuthor() {
        val args = mapOf("name" to "test_author")
        assert(impl?.createAuthor(args) != null)
    }

}