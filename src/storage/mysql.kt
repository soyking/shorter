package storage

import java.sql.DriverManager
import java.sql.Connection

class MySQLStorageDAOImpl(url: String, username: String, password: String) : StorageDAO() {
    var connection: Connection

    init {
        connection = DriverManager
                .getConnection(url, username, password)
    }

    override fun createAuthor(args: Map<String, Any>): Author? {
        val name = args["name"] as? String ?: return null

        val author = Author(
                id = getUUID(),
                name = name,
                createdAt = System.currentTimeMillis(),
                key = getUUID()
        )

        val pstmt = connection.prepareStatement(
                "insert into " + this.TABLE_AUTHOR + " (id, name, created_at, `key`) " +
                        "values (?, ?, ?, ?)"
        )
        pstmt.setString(1, author.id)
        pstmt.setString(2, author.name)
        pstmt.setLong(3, author.createdAt)
        pstmt.setString(4, author.key)
        pstmt.executeUpdate()
        pstmt.close()

        return author
    }

    override fun getAuthor(id: String): Author? {
        val pstmt = connection.prepareStatement(
                "select * from " + this.TABLE_AUTHOR + " where id=?"
        )
        pstmt.setString(1, id)
        val rs = pstmt.executeQuery()

        var author: Author? = null
        if (rs.next()) {
            author = Author(
                    id = rs.getString("id"),
                    name = rs.getString("name"),
                    createdAt = rs.getLong("created_at"),
                    key = rs.getString("key")
            )
        }

        pstmt.close()
        return author
    }

    override fun createSheet(args: Map<String, Any>): Sheet? {
        val typeString = args["type"] as? String ?: return null
        val text = args["text"] as? String ?: return null
        val link = args["link"] as? String
        val authorID = args["author"] as? String ?: return null
        this.getAuthor(authorID) ?: return null

        // valid sheet type
        val type: SheetType
        try {
            type = SheetType.valueOf(typeString)
            if (type == SheetType.LINK && link == null) {
                return null
            }
        } catch (_: IllegalArgumentException) {
            return null
        }

        val sheet = Sheet(
                id = getUUID(),
                createdAt = System.currentTimeMillis(),
                author = authorID,
                type = type,
                text = text,
                link = link
        )

        val pstmt = connection.prepareStatement(
                "insert into " + this.TABLE_SHEET + " (id, created_at, author, type, text, link) " +
                        "values (?, ?, ?, ?, ?, ?)"
        )
        pstmt.setString(1, sheet.id)
        pstmt.setLong(2, sheet.createdAt)
        pstmt.setString(3, sheet.author)
        pstmt.setString(4, sheet.type.toString())
        pstmt.setString(5, sheet.text)
        pstmt.setString(6, sheet.link)
        pstmt.executeUpdate()

        return sheet
    }

    override fun getSheets(pages: Int, count: Int): Array<Sheet> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSheet(id: String): Sheet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
