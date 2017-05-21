package storage

import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder


class MyBatisStorageDAOImpl(configPath: String) : StorageDAO {
    val sqlSessionFactory: SqlSessionFactory

    init {
        val inputStream = Resources.getResourceAsStream(configPath);
        sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
    }

    override fun createAuthor(params: Map<String, Any>) {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(StorageDAO::class.java)
            authorMapper.createAuthor(params)
            session.commit()
        }
    }

    override fun getAuthor(id: String): Author? {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(StorageDAO::class.java)
            val author = authorMapper.getAuthor(id)
            return author
        }
    }

    override fun createSheet(id: String, createdAt: Long, author: String, type: SheetType, text: String, link: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSheets(args: Map<String, Any>): ArrayList<Sheet>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
