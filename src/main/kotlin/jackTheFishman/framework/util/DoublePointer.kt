package jackTheFishman.framework.util

class DoublePointer {
    private val array = doubleArrayOf(0.0)

    val buffer: DoubleArray
        get() = array

    val value: Double
        get() = array[0]
}
