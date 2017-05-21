package storage

import java.sql.*

class MySQLStorageDAOImpl(val url: String, val username: String, val password: String) : StorageDAO {
    var connection: Connection = DriverManager
        .getConnection(url, username, password)
    val TABLE_AUTHOR = "author"
    val TABLE_SHEET = "sheet"

    override fun createAuthor(params: Map<String, Any>) {
        val pstmt = connection.prepareStatement(
            "insert into " + TABLE_AUTHOR + " (id, name, created_at, `key`, secret) " +
                "values (?, ?, ?, ?, ?)"
        )
        pstmt.setString(1, params["id"] as String)
        pstmt.setString(2, params["name"] as String)
        pstmt.setLong(3, params["created_at"] as Long)
        pstmt.setString(4, params["key"] as String)
        pstmt.setString(5, params["secret"] as String)
        pstmt.executeUpdate()
        pstmt.close()
    }

    override fun getAuthor(id: String): Author? {
        val pstmt = connection.prepareStatement(
            "select * from $TABLE_AUTHOR where id=?"
        )
        pstmt.setString(1, id)
        val rs = pstmt.executeQuery()

        var author: Author? = null
        if (rs.next()) {
            author = Author(
                id = rs.getString("id"),
                name = rs.getString("name"),
                createdAt = rs.getLong("created_at"),
                key = rs.getString("key"),
                secret = rs.getString("secret")
            )
        }

        pstmt.close()
        return author
    }

    override fun createSheet(id: String, createdAt: Long, author: String, type: SheetType, text: String, link: String) {
        val pstmt = connection.prepareStatement(
            "insert into " + TABLE_SHEET + " (id, created_at, author, type, text, link) " +
                "values (?, ?, ?, ?, ?, ?)"
        )
        pstmt.setString(1, id)
        pstmt.setLong(2, createdAt)
        pstmt.setString(3, author)
        pstmt.setString(4, type.toString())
        pstmt.setString(5, text)
        pstmt.setString(6, link)
        pstmt.executeUpdate()
        pstmt.close()
    }

    private fun getSheetsResult(rs: ResultSet): ArrayList<Sheet>? {
        val sheetList = ArrayList<Sheet>()
        while (rs.next()) {
            val typeString = rs.getString("type")
            val type = SheetType.valueOf(typeString)
            sheetList.add(
                Sheet(
                    id = rs.getString("id"),
                    createdAt = rs.getLong("created_at"),
                    author = rs.getString("author"),
                    type = type,
                    text = rs.getString("text"),
                    link = rs.getString("link")
                )
            )
        }
        return sheetList
    }

    override fun getSheets(args: Map<String, Any>): ArrayList<Sheet>? {
        val sheetID = args["id"] as? String
        val rs: ResultSet
        val pstmt: PreparedStatement
        if (sheetID != null) {
            pstmt = connection.prepareStatement(
                "select $TABLE_SHEET.*, $TABLE_AUTHOR.name " +
                    "from $TABLE_SHEET join $TABLE_AUTHOR ON $TABLE_SHEET.author=$TABLE_AUTHOR.id " +
                    "where $TABLE_SHEET.id=?"
            )
            pstmt.setString(1, sheetID)
            rs = pstmt.executeQuery()
        } else {
            val pages = (args["pages"] as? String)?.toInt() ?: 1
            val count = (args["count"] as? String)?.toInt() ?: 10
            val offset = (pages - 1) * count

            pstmt = connection.prepareStatement(
                "select $TABLE_SHEET.*, $TABLE_AUTHOR.name " +
                    "from $TABLE_SHEET join $TABLE_AUTHOR ON $TABLE_SHEET.author=$TABLE_AUTHOR.id " +
                    "order by created_at desc limit ?, ?"
            )
            pstmt.setInt(1, offset)
            pstmt.setInt(2, count)
            rs = pstmt.executeQuery()
        }

        val result = getSheetsResult(rs)
        pstmt.close()
        return result
    }
}
