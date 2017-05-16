package storage

import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import java.io.FileInputStream
import java.io.File




class MyBatisStorageDAOImpl(configPath: String) : StorageDAO() {
    val sqlSessionFactory: SqlSessionFactory

    init {
        val file = File(configPath)
        val inputStream = FileInputStream(file)
        sqlSessionFactory = SqlSessionFactoryBuilder().build(inputStream)
    }

    override fun createAuthor(args: Map<String, Any>): Author? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSheet(args: Map<String, Any>): Sheet? {
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
