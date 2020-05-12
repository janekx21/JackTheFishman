package engine.math

import org.joml.Vector3f
import org.joml.Vector3fc


fun Vector3f.clamp(value: Float) {
    if (lengthSquared() > 0) {
        val length = length().coerceAtMost(value)
        normalize()
        mul(length)
    }
}

operator fun Vector3f.times(other: Vector3f): Vector3f {
    return Vector3f(this).mul(other)
}

object Const {
    val zero: Vector3fc = Vector3f(0f, 0f, 0f)
    val one: Vector3fc = Vector3f(1f, 1f, 1f)

    val forward: Vector3fc = Vector3f(0f, 0f, -1f)
    val backwards: Vector3fc = Vector3f(0f, 0f, 1f)
    val right: Vector3fc = Vector3f(1f, 0f, 0f)
    val left: Vector3fc = Vector3f(-1f, 0f, 0f)
    val up: Vector3fc = Vector3f(0f, 1f, 0f)
    val down: Vector3fc = Vector3f(0f, -1f, 0f)
}

object Copy {
    val zero: Vector3f get() = Vector3f(0f, 0f, 0f)
    val one: Vector3f get() = Vector3f(1f, 1f, 1f)

    val forward: Vector3f get() = Vector3f(0f, 0f, -1f)
    val backwards: Vector3f get() = Vector3f(0f, 0f, 1f)
    val right: Vector3f get() = Vector3f(1f, 0f, 0f)
    val left: Vector3f get() = Vector3f(-1f, 0f, 0f)
    val up: Vector3f get() = Vector3f(0f, 1f, 0f)
    val down: Vector3f get() = Vector3f(0f, -1f, 0f)
}
