package jackTheFishman.engine.math

import jackTheFishman.engine.util.IJsonUnserializable
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

fun Vector3f.toJson(): Any? {
    return arrayOf(
        this.x, this.y, this.z
    )
}

object Vector3fExt : IJsonUnserializable<Vector3f> {
    override fun fromJson(json: Any?): Vector3f {
        val list = json as List<*>
        return Vector3f(
            (list[0] as Double).toFloat(),
            (list[1] as Double).toFloat(),
            (list[2] as Double).toFloat()
        )
    }
}