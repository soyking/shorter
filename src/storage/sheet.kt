package storage

enum class SheetType {
    TEXT, LINK
}

class Sheet(
    var id: String,
    var createdAt: Long,
    var author: Author,
    var type: SheetType,
    var text: String,
    var link: String?
)

interface SheetDAO {
    fun createSheet(args: Map<String, Any>): Sheet?
    fun getSheets(args: Map<String, Any>): ArrayList<Sheet>?
}
