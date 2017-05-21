package storage

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder


class MyBatisStorageDAOImpl(configPath: String) : StorageDAO() {
    val sqlSessionFactory: SqlSessionFactory

    init {
        val inputStream = Resources.getResourceAsStream(configPath);
        sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
    }

    override fun createAuthor(id: String, createdAt: Long, name: String, key: String, secret: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSheet(id: String, createdAt: Long, author: String, type: SheetType, text: String, link: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getAuthor(id: String): Author? {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(AuthorDAO::class.java)
            val author = authorMapper.getAuthor(id)
            return author
        }
    }

    override fun getSheets(args: Map<String, Any>): ArrayList<Sheet>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
