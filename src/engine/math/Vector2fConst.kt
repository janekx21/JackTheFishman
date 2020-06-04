package engine.math

import org.joml.Vector2f
import org.joml.Vector2fc

object Vector2fConst {
    val zero: Vector2fc = Vector2f(0f, 0f)
    val one: Vector2fc = Vector2f(1f, 1f)

    // Da gibts glaube kein gutes Äquivalent bei Vector2f
    // val forward: Vector3fc = Vector3f(0f, 0f, -1f)
    // val backwards: Vector3fc = Vector3f(0f, 0f, 1f)
    val right: Vector2fc = Vector2f(1f, 0f)
    val left: Vector2fc = Vector2f(-1f, 0f)
    val up: Vector2fc = Vector2f(0f, 1f)
    val down: Vector2fc = Vector2f(0f, -1f)
}