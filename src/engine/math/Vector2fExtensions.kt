package engine.math

import engine.util.IJsonUnserializable
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f

fun Vector2f.clamp(value: Float) {
    if (lengthSquared() > 0) {
        val length = length().coerceAtMost(value)
        normalize()
        mul(length)
    }
}

operator fun Vector2f.times(other: Vector2fc): Vector2f {
    return Vector2f(this).mul(other)
}

operator fun Vector2f.times(other: Float): Vector2f {
    return Vector2f(this).mul(other)
}

operator fun Vector2f.timesAssign(other: Vector2fc) {
    mul(other)
}

operator fun Vector2f.timesAssign(other: Float) {
    mul(other)
}

operator fun Vector2f.plus(other: Vector2fc): Vector2f {
    return Vector2f(this).add(other)
}

operator fun Vector2f.plusAssign(other: Vector2fc) {
    add(other)
}

operator fun Vector2f.unaryMinus(): Vector2f {
    return Vector2f(this).mul(-1f)
}

fun Vector2f.toJson(): Any? {
    return arrayOf(this.x, this.y)
}

object Vector2fExt : IJsonUnserializable<Vector2f> {
    override fun fromJson(json: Any?): Vector2f {
        val array = json as Array<*>
        return Vector2f(
            FloatExt.fromJson(array[0]),
            FloatExt.fromJson(array[1])
        )
    }
}