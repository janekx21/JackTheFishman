package jackTheFishman.framework.util.pointer

class DoublePointer {
    private val array = doubleArrayOf(0.0)

    val buffer: DoubleArray
        get() = array

    val value: Double
        get() = array[0]
}
