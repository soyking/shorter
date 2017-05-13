package storage

interface StorageDAO : AuthorDAO, SheetDAO

var storageDAOImpl: StorageDAO? = null

fun init() {
    val storageDAOImpl = null
}