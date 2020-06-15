package engine.math

import org.joml.Vector2f

fun Vector2f.clamp(value: Float) {
    if (lengthSquared() > 0) {
        val length = length().coerceAtMost(value)
        normalize()
        mul(length)
    }
}

operator fun Vector2f.times(other: Vector2f): Vector2f {
    return Vector2f(this).mul(other)
}

operator fun Vector2f.times(other: Float): Vector2f {
    return Vector2f(this).mul(other)
}

operator fun Vector2f.timesAssign(other: Vector2f) {
    mul(other)
}

operator fun Vector2f.timesAssign(other: Float) {
    mul(other)
}

operator fun Vector2f.plus(other: Vector2f): Vector2f {
    return Vector2f(this).add(other)
}

operator fun Vector2f.plusAssign(other: Vector2f) {
    add(other)
}

operator fun Vector2f.unaryMinus(): Vector2f {
    return Vector2f(this).mul(-1f)
}
