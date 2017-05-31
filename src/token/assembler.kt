package token

interface Assembler {
    fun combine(parts: List<String?>?): String?
    fun extract(text: String?): List<String>?
}

class SplitAssembler : Assembler {
    val SEPARATOR = "|"

    override fun combine(parts: List<String?>?): String? {
        return parts?.joinToString(separator = SEPARATOR)
    }

    override fun extract(text: String?): List<String>? {
        return text?.split(delimiters = SEPARATOR)
    }
}