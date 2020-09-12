package jackTheFishman.framework.math.extentions

import org.joml.Vector3f
import org.joml.Vector3fc

fun Vector3f.clamp(value: Float) {
    if (lengthSquared() > 0) {
        val length = length().coerceAtMost(value)
        normalize()
        mul(length)
    }
}

operator fun Vector3f.times(other: Vector3fc): Vector3fc {
    return Vector3f(this).mul(other)
}

operator fun Vector3f.times(other: Float): Vector3fc {
    return Vector3f(this).mul(other)
}

operator fun Vector3f.timesAssign(other: Vector3fc) {
    mul(other)
}

operator fun Vector3f.timesAssign(other: Float) {
    mul(other)
}

operator fun Vector3f.plus(other: Vector3fc): Vector3fc {
    return Vector3f(this).add(other)
}

operator fun Vector3f.minus(other: Vector3fc): Vector3fc {
    return Vector3f(this).sub(other)
}

operator fun Vector3f.plusAssign(other: Vector3fc) {
    add(other)
}

operator fun Vector3f.unaryMinus(): Vector3fc {
    return Vector3f(this).mul(-1f)
}
