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

    override fun createAuthor(params: Map<String, Any?>) {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(StorageDAO::class.java)
            authorMapper.createAuthor(params)
            session.commit()
        }
    }

    override fun getAuthor(name: String): Author? {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(StorageDAO::class.java)
            val author = authorMapper.getAuthor(name)
            return author
        }
    }

    override fun createSheet(params: Map<String, Any?>) {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(StorageDAO::class.java)
            authorMapper.createSheet(params)
            session.commit()
        }
    }

    override fun getSheets(params: Map<String, Any>): ArrayList<Sheet>? {
        val session = sqlSessionFactory.openSession()
        session.use { session ->
            val authorMapper = session.getMapper(StorageDAO::class.java)
            return authorMapper.getSheets(params)
        }
    }
}
