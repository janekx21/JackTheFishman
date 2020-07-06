package engine.math

import org.joml.Vector3f
import org.joml.Vector3fc

fun Vector3fc.clamp(value: Float): Vector3fc {
    val result = Vector3f(this)
    if (lengthSquared() > 0) {
        val length = length().coerceAtMost(value)
        result.normalize()
        result *= length
    }
    return result
}

operator fun Vector3fc.times(other: Vector3fc): Vector3fc {
    return Vector3f(this).mul(other)
}

operator fun Vector3fc.times(other: Float): Vector3fc {
    return Vector3f(this).mul(other)
}

operator fun Vector3fc.plus(other: Vector3fc): Vector3fc {
    return Vector3f(this).add(other)
}

operator fun Vector3fc.minus(other: Vector3fc): Vector3fc {
    return Vector3f(this).sub(other)
}

operator fun Vector3fc.unaryMinus(): Vector3fc {
    return Vector3f(this).negate()
}
