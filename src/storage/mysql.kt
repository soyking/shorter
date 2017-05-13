package storage

import java.sql.DriverManager
import java.sql.Connection

class MySQLStorageDAOImpl(url: String, username: String, password: String) : StorageDAO {
    var connection: Connection

    init {
        connection = DriverManager
                .getConnection(url, username, password)
    }

    override fun createAuthor(args: Map<String, Any>): Author? {
        println("in create author" + args["name"])
        val name = args["name"] as String? ?: return null

        val author = Author(
                id = getUUID(),
                name = name,
                createdAt = System.currentTimeMillis(),
                key = getUUID()
        )

        val pstmt = connection.prepareStatement(
                "insert into author values (?, ?, ?, ?)"
        )
        pstmt.setString(1, author.id)
        pstmt.setString(2, author.name)
        pstmt.setLong(3, author.createdAt!!)
        pstmt.setString(4, author.key)
        pstmt.executeUpdate()

        return author
    }

    override fun getAuthor(id: String): Author? {
        val stmt = connection.createStatement()
        val sql = "SELECT name FROM author"
        val rs = stmt.executeQuery(sql)

        if (rs.next()) {
            val name = rs.getString("name")
            return Author(name = name)
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
