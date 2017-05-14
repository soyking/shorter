package storage

enum class SheetType {
    TEXT, LINK
}

class Sheet(
        var id: String,
        var createdAt: Long,
        var author: String,
        var type: SheetType,
        var text: String,
        var link: String?
)

interface SheetDAO {
    fun createSheet(args: Map<String, Any>): Sheet?
    fun getSheets(pages: Int, count: Int): Array<Sheet>?
    fun getSheet(id: String): Sheet?
}
