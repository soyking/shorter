package storage

fun anyToInt(v: Any?, default: Int): Int {
    return (v as? String)?.toInt() ?: (v as? Int) ?: default
}
