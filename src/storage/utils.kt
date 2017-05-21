package storage

import java.util.*

fun getUUID(): String {
    return UUID.randomUUID().toString()
}

fun anyToInt(v: Any?, default: Int): Int {
    return (v as? String)?.toInt() ?: (v as? Int) ?: default
}
