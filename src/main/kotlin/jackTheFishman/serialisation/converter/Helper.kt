package jackTheFishman.serialisation.converter

fun anyAsFloat(value: Any?): Float {
    checkNotNull(value) { "Value is null" }
    check(value is Double) { "Value is not convertible" }
    return value.toFloat()
}
