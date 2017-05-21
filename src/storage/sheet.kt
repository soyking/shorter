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
    fun createSheet(
        id: String,
        createdAt: Long,
        author: String,
        type: SheetType,
        text: String,
        link: String
    )

    fun getSheets(args: Map<String, Any>): ArrayList<Sheet>?
}
