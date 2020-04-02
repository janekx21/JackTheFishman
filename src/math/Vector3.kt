package math

import kotlin.math.sqrt

data class Vector3(val x: Float, val y: Float, val z: Float) {
    operator fun plus(other: Vector3): Vector3 {
        return Vector3(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3): Vector3 {
        return Vector3(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(scalar: Float): Vector3 {
        return Vector3(x * scalar, y * scalar, z * scalar)
    }

    operator fun div(scalar: Float): Vector3 {
        return Vector3(x / scalar, y / scalar, z / scalar)
    }

    fun normalized(): Vector3 {
        val mag = magnitude()
        if (mag == 0f) {
            return zero
        }
        return this / magnitude()
    }

    fun magnitude(): Float = sqrt(x * x + y * y + z * z)

    companion object {
        val zero = Vector3(0f, 0f, 0f)
        val one = Vector3(1f, 1f, 1f)
        val right = Vector3(1f, 0f, 0f)
        val up = Vector3(0f, 1f, 0f)
        val forward = Vector3(0f, 0f, -1f)
    }
}