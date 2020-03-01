import kotlin.math.sqrt

data class Vector(val x: Float, val y: Float) {
    operator fun plus(delta: Vector): Vector {
        return Vector(delta.x + x, delta.y + y)
    }

    operator fun minus(delta: Vector): Vector {
        return Vector(x - delta.x, y - delta.y)
    }

    operator fun times(value: Float): Vector {
        return Vector(x * value, y * value)
    }

    fun magnetude(): Float {
        return sqrt(x*x + y*y)
    }

    companion object {
        val zero = Vector(0f, 0f)
        val one = Vector(1f, 1f)
        val right = Vector(1f, 0f)
        val left = Vector(-1f, 0f)
        val up = Vector(0f, 1f)
        val down = Vector(0f, -1f)

        fun lerp(a: Vector, b: Vector, t: Float) : Vector {
            return Vector(a.x * (1-t) + b.x * t, a.y * (1-t) + b.y * t)
        }
    }
}