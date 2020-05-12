package engine.math

import org.joml.Vector3f

object Copy {
    val zero: Vector3f get() = Vector3f(0f, 0f, 0f)
    val one: Vector3f get() = Vector3f(1f, 1f, 1f)

    val forward: Vector3f get() = Vector3f(0f, 0f, -1f)
    val backwards: Vector3f get() = Vector3f(0f, 0f, 1f)
    val right: Vector3f get() = Vector3f(1f, 0f, 0f)
    val left: Vector3f get() = Vector3f(-1f, 0f, 0f)
    val up: Vector3f get() = Vector3f(0f, 1f, 0f)
    val down: Vector3f get() = Vector3f(0f, -1f, 0f)
}