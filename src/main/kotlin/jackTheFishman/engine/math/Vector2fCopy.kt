package jackTheFishman.engine.math

import org.joml.Vector2f

object Vector2fCopy {
    val zero: Vector2f get() = Vector2f(0f, 0f)
    val one: Vector2f get() = Vector2f(1f, 1f)

    // val forward: Vector3f get() = Vector3f(0f, 0f, -1f)
    // val backwards: Vector3f get() = Vector3f(0f, 0f, 1f)
    val right: Vector2f get() = Vector2f(1f, 0f)
    val left: Vector2f get() = Vector2f(-1f, 0f)
    val up: Vector2f get() = Vector2f(0f, 1f)
    val down: Vector2f get() = Vector2f(0f, -1f)
}