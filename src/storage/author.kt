package storage

class Author(
        var id: String,
        var createdAt: Long,
        var name: String,
        var key: String
)

interface AuthorDAO {
    fun createAuthor(args: Map<String, Any>): Author?
    fun getAuthor(id: String): Author?
}