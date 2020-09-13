package jackTheFishman.util.pointer

class DoublePointer {
    private val single = doubleArrayOf(0.0)

    val array: DoubleArray
        get() = single

    val value: Double
        get() = single[0]
}
