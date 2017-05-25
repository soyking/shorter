package storage

interface StorageDAO : AuthorDAO, SheetDAO

val storageDAO: StorageDAO = MyBatisStorageDAOImpl("mybatis/mybatis.xml")