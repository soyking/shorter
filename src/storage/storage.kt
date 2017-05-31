package storage

interface StorageDAO : AuthorDAO, SheetDAO

var storageDAO: StorageDAO = MyBatisStorageDAOImpl("mybatis/mybatis.xml")