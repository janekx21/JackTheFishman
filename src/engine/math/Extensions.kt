package engine.math

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.assimp.AIMatrix4x4


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


fun AIMatrix4x4.toMatrix4f(): Matrix4f {
    return Matrix4f(
        a1(), a2(), a3(), a4(),
        b1(), b2(), b3(), b4(),
        c1(), c2(), c3(), c4(),
        d1(), d2(), d3(), d4()
    )
}