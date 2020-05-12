package engine.math

import org.joml.Vector3f


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

