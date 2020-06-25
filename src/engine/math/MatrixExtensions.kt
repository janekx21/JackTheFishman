package engine.math

import org.joml.Matrix4f

fun Matrix4f.toJson(): Any? {
    return arrayOf(
        this[0, 0], this[1, 0], this[2, 0], this[3, 0],
        this[0, 1], this[1, 1], this[2, 1], this[3, 1],
        this[0, 2], this[1, 2], this[2, 2], this[3, 2],
        this[0, 3], this[1, 3], this[2, 3], this[3, 3]
    )
}

fun Matrix4f.fromJson(json: Any?) {
    val array = json as List<*>
    (0 .. 15).forEach {
        this[it % 4, it / 4] = (array[it] as Double).toFloat()
    }
}