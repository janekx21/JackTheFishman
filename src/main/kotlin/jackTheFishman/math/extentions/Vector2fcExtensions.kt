package jackTheFishman.math.extentions

import org.joml.Vector2f
import org.joml.Vector2fc

fun Vector2fc.clamp(value: Float): Vector2fc {
    val result = Vector2f(this)
    if (lengthSquared() > 0) {
        val length = length().coerceAtMost(value)
        result.normalize()
        result *= length
    }
    return result
}

fun Vector2fc.moveTowards(target: Vector2fc, maxDelta: Float): Vector2fc {
    val delta = target - this
    val clampedDelta = delta.clamp(maxDelta)
    return this + clampedDelta
}

operator fun Vector2fc.times(other: Vector2fc): Vector2fc {
    return Vector2f(this).mul(other)
}

operator fun Vector2fc.times(other: Float): Vector2fc {
    return Vector2f(this).mul(other)
}

operator fun Vector2fc.plus(other: Vector2fc): Vector2fc {
    return Vector2f(this).add(other)
}

operator fun Vector2fc.minus(other: Vector2fc): Vector2fc {
    return Vector2f(this).sub(other)
}

operator fun Vector2fc.unaryMinus(): Vector2fc {
    return Vector2f(this).negate()
}
