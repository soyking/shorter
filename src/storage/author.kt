package storage

class Author(
    var id: String? = null,
    var createdAt: Long? = null,
    var name: String? = null,
    var key: String? = null,
    var secret: String? = null
)

interface AuthorDAO {
    fun createAuthor(
        id: String,
        createdAt: Long,
        name: String,
        key: String,
        secret: String
    )

    fun getAuthor(id: String): Author?
}