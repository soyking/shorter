package storage

class Author(
    var id: String,
    var createdAt: Long? = null,
    var name: String,
    var key: String? = null
)

interface AuthorDAO {
    fun createAuthor(args: Map<String, Any>): Author?
    fun getAuthor(id: String): Author?
}