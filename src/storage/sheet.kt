package storage

enum class SheetType {
    TEXT, LINK
}

class Sheet(
    var id: String? = null,
    var createdAt: Long? = null,
    var author: String? = null,
    var type: String? = null,
    var text: String? = null,
    var link: String? = null,
    var token: String? = null
)

interface SheetDAO {
    fun createSheet(params: Map<String, Any?>)
    fun getSheets(params: Map<String, Any>): ArrayList<Sheet>?
}
