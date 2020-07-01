package engine.math

import org.jbox2d.common.Vec2
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f
import org.lwjgl.assimp.AIMatrix4x4
import org.lwjgl.assimp.AIVector2D
import org.lwjgl.assimp.AIVector3D

fun AIMatrix4x4.toMatrix4f(): Matrix4f {
    return Matrix4f(
        a1(), a2(), a3(), a4(),
        b1(), b2(), b3(), b4(),
        c1(), c2(), c3(), c4(),
        d1(), d2(), d3(), d4()
    )
}

fun AIVector3D.toVector3f(): Vector3f {
    return Vector3f(x(), y(), z())
}

fun AIVector2D.toVector2f(): Vector2f {
    return Vector2f(x(), y())
}

fun Vec2.toVector2fc(): Vector2fc {
    return Vector2f(x, y)
}

fun Vector2fc.toVec2(): Vec2 {
    return Vec2(x(), y())
}
