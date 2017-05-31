package storage

import java.util.*

fun getUUID(): String {
    return UUID.randomUUID().toString()
}

fun anyToInt(v: Any?, default: Int): Int {
    return (v as? String)?.toInt() ?: (v as? Int) ?: default
}

private val randomChar = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
var ran = Random()

fun randomString(len: Int): String {
    val sb = StringBuilder(len)
    for (i in 0..len - 1)
        sb.append(randomChar[ran.nextInt(randomChar.length)])
    return sb.toString()
}