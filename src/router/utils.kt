package router

import java.util.*

private val randomChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
var ran = Random()

fun randomString(len: Int): String {
    val sb = StringBuilder(len)
    for (i in 0..len - 1)
        sb.append(randomChar[ran.nextInt(randomChar.length)])
    return sb.toString()
}

fun getKey(): String {
    return randomString(16)
}

fun getUUID(): String {
    return UUID.randomUUID().toString()
}

val DuplicateAuthorErr = APIException("duplicate author name")
val ExceedMaxSheetsLimitErr = APIException("exceed max sheets limit")
val EmptyTextErr = APIException("empty text")
val WithoutLinkErr = APIException("without link")
val InvalidSheetTypeErr = APIException("invalid sheet type")
val ReuseTokenErr = APIException("token has been used")
