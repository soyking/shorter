package storage

class Author(
    var id: String? = null,
    var createdAt: Long? = null,
    var name: String? = null,
    var key: String? = null,
    var secret: String? = null
)

interface AuthorDAO {
    fun createAuthor(params: Map<String, Any>)
    fun getAuthor(id: String): Author?
}