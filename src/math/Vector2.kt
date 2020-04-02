package math

data class Vector2(val x: Float, val y: Float) {
    operator fun minus(other: Vector2): Vector2 {
        return Vector2(x - other.x, y - other.y)
    }

    companion object {
        val zero = Vector2(0f, 0f)
    }
}