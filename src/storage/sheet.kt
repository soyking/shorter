package storage

enum class SheetType {
    TEXT, LINK
}

class Sheet(
    var id: String?,
    var createdAt: Long?,
    var author: String?,
    var type: String?,
    var text: String?,
    var link: String?,
    var token: String?
)

interface SheetDAO {
    fun createSheet(params: Map<String, Any?>)
    fun getSheets(params: Map<String, Any>): ArrayList<Sheet>?
}
