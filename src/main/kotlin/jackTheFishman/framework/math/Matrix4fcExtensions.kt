package jackTheFishman.framework.math

import org.joml.Matrix4f
import org.joml.Matrix4fc

operator fun Matrix4fc.times(other: Matrix4fc): Matrix4fc {
    return Matrix4f(this).mul(other)
}

fun Matrix4fc.toJson(): Any? {
    return arrayOf(
        this[0, 0], this[1, 0], this[2, 0], this[3, 0],
        this[0, 1], this[1, 1], this[2, 1], this[3, 1],
        this[0, 2], this[1, 2], this[2, 2], this[3, 2],
        this[0, 3], this[1, 3], this[2, 3], this[3, 3]
    )
}
