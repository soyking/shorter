package storage

class Author(
        var id: String,
        var createdAt: Int,
        var name: String,
        var key: String
)

interface AuthorDAO {
    fun getAuthor(id: String): Author
}