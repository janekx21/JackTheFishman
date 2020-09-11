package jackTheFishman.framework.util

fun anyAsFloat(value: Any?): Float {
    check(value != null) { "value not found" }
    check(value is Double) { "value is not a Float" }
    return value.toFloat()
}
