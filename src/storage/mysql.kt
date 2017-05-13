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
                "insert into " + this.TABLE_AUTHOR + " values (?, ?, ?, ?)"
        )
        pstmt.setString(1, author.id)
        pstmt.setString(2, author.name)
        pstmt.setLong(3, author.createdAt!!)
        pstmt.setString(4, author.key)
        pstmt.executeUpdate()

        return author
    }

    override fun getAuthor(id: String): Author? {
        val pstmt = connection.prepareStatement(
                "select * from " + this.TABLE_AUTHOR + " where id=?"
        )
        pstmt.setString(1, id)
        val rs = pstmt.executeQuery()

        if (rs.next()) {
            return Author(
                    id = rs.getString("id"),
                    name = rs.getString("name"),
                    createdAt = rs.getLong("created_at"),
                    key = rs.getString("key")
            )
        }

        return null
    }

    override fun createSheet(args: Map<String, Any>): Sheet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSheets(pages: Int, count: Int): Array<Sheet> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSheet(id: String): Sheet {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
