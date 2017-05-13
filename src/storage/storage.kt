package storage

abstract class StorageDAO : AuthorDAO, SheetDAO {
    val TABLE_AUTHOR = "author"
    val TABLE_SHEET = "sheet"
}

var storageDAOImpl: StorageDAO? = null

fun init() {
    val storageDAOImpl = null
}