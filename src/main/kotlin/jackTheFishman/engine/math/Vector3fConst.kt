package jackTheFishman.engine.math

import org.joml.Vector3f
import org.joml.Vector3fc

object Vector3fConst {
    val zero: Vector3fc = Vector3f(0f, 0f, 0f)
    val one: Vector3fc = Vector3f(1f, 1f, 1f)

    val forward: Vector3fc = Vector3f(0f, 0f, -1f)
    val backwards: Vector3fc = Vector3f(0f, 0f, 1f)
    val right: Vector3fc = Vector3f(1f, 0f, 0f)
    val left: Vector3fc = Vector3f(-1f, 0f, 0f)
    val up: Vector3fc = Vector3f(0f, 1f, 0f)
    val down: Vector3fc = Vector3f(0f, -1f, 0f)
}