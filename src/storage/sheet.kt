package storage

enum class SheetType {
    TEXT, LINK
}

class Sheet(
    var id: String?,
    var createdAt: Long?,
    var author: String?,
    var type: SheetType?,
    var text: String?,
    var link: String?
) {
    constructor(id: String?,
                createdAt: Long?,
                author: String?,
                type: String?,
                text: String?,
                link: String?) : this(
        id = id,
        createdAt = createdAt,
        author = author,
        type = SheetType.valueOf(type!!),
        text = text,
        link = link)
}

interface SheetDAO {
    fun createSheet(params: Map<String, Any>)
    fun getSheets(params: Map<String, Any>): ArrayList<Sheet>?
}
