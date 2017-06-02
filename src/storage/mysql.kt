package storage

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class MySQLStorageDAOImpl(val url: String, val username: String, val password: String) : StorageDAO {
    var connection: Connection = DriverManager
        .getConnection(url, username, password)
    val TABLE_AUTHOR = "author"
    val TABLE_SHEET = "sheet"

    override fun createAuthor(params: Map<String, Any?>) {
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

    override fun createSheet(params: Map<String, Any?>) {
        val pstmt = connection.prepareStatement(
            "insert into " + TABLE_SHEET + " (id, created_at, author, type, text, link, token) " +
                "values (?, ?, ?, ?, ?, ?, ?)"
        )
        pstmt.setString(1, params["id"] as String)
        pstmt.setLong(2, params["created_at"] as Long)
        pstmt.setString(3, params["author"] as String)
        pstmt.setString(4, params["type"] as String)
        pstmt.setString(5, params["text"] as String)
        pstmt.setString(6, params["link"] as String)
        pstmt.setString(7, params["token"] as String)
        pstmt.executeUpdate()
        pstmt.close()
    }

    private fun getSheetsResult(rs: ResultSet): ArrayList<Sheet>? {
        val sheetList = ArrayList<Sheet>()
        while (rs.next()) {
            sheetList.add(
                Sheet(
                    id = rs.getString("id"),
                    createdAt = rs.getLong("created_at"),
                    author = rs.getString("author"),
                    type = rs.getString("type"),
                    text = rs.getString("text"),
                    link = rs.getString("link"),
                    token = rs.getString("token")
                )
            )
        }
        return sheetList
    }

    override fun getSheets(params: Map<String, Any>): ArrayList<Sheet>? {
        val sheetID = params["id"] as? String
        val token = params["token"] as? String
        val rs: ResultSet
        val pstmt: PreparedStatement
        if (sheetID != null) {
            pstmt = connection.prepareStatement(
                "select * from $TABLE_SHEET where id=?"
            )
            pstmt.setString(1, sheetID)
            rs = pstmt.executeQuery()
        } else if (token != null) {
            pstmt = connection.prepareStatement(
                "select * from $TABLE_SHEET where token=?"
            )
            pstmt.setString(1, token)
            rs = pstmt.executeQuery()
        } else {
            val offset = anyToInt(params["offset"], 0)
            val count = anyToInt(params["count"], 10)

            pstmt = connection.prepareStatement(
                "select * from $TABLE_SHEET order by created_at desc limit ?, ?"
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
