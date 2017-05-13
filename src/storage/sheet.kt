package storage

class Sheet(
        var id: String,
        var createdAt: Long,
        var author: Int,
        var type: String,
        var text: String,
        var link: String
)

interface SheetDAO {
    fun createSheet(args: Map<String, Any>): Sheet?
    fun getSheets(pages: Int, count: Int): Array<Sheet>?
    fun getSheet(id: String): Sheet?
}
