package jackTheFishman.engine.util

class FloatPointer {
    private val array = floatArrayOf(0f)

    val buffer: FloatArray
        get() = array

    var value: Float
        get() = array[0]
        set(value) { array[0] = value }
}
