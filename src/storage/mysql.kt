package storage

import java.sql.DriverManager
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

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
            "insert into " + TABLE_AUTHOR + " (id, name, created_at, `key`) " +
                "values (?, ?, ?, ?)"
        )
        pstmt.setString(1, author.id)
        pstmt.setString(2, author.name)
        pstmt.setLong(3, author.createdAt!!)
        pstmt.setString(4, author.key)
        pstmt.executeUpdate()
        pstmt.close()

        return author
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
        val author = getAuthor(authorID) ?: return null

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
            author = author,
            type = type,
            text = text,
            link = link
        )

        val pstmt = connection.prepareStatement(
            "insert into " + TABLE_SHEET + " (id, created_at, author, type, text, link) " +
                "values (?, ?, ?, ?, ?, ?)"
        )
        pstmt.setString(1, sheet.id)
        pstmt.setLong(2, sheet.createdAt)
        pstmt.setString(3, authorID)
        pstmt.setString(4, sheet.type.toString())
        pstmt.setString(5, sheet.text)
        pstmt.setString(6, sheet.link)
        pstmt.executeUpdate()
        pstmt.close()

        return sheet
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
                    author = Author(id = rs.getString("author"), name = rs.getString("name")),
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
