package engine.util

class IntPointer {
    private val array = intArrayOf(0)

    val buffer: IntArray
        get() = array

    val value: Int
        get() = array[0]
}