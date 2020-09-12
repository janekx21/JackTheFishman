package jackTheFishman.framework.util

import java.nio.IntBuffer

class IntPointer {
    private val single = intArrayOf(0)

    val array: IntArray
        get() = single

    val buffer: IntBuffer
        get() = IntBuffer.wrap(single)

    val value: Int
        get() = single[0]
}
