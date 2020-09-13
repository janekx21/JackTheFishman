package jackTheFishman

import jackTheFishman.math.extentions.*
import org.joml.Vector2f
import org.joml.Vector2fc
import org.junit.Assert
import kotlin.test.Test

class Vector2fcExtensionsTests {
    @Test
    fun shouldAdd() {
        val vectorA: Vector2fc = Vector2f(1f, 2f)
        val vectorB: Vector2fc = Vector2f(3f, 3f)

        Assert.assertEquals(Vector2f(4f, 5f), vectorA + vectorB)
    }

    @Test
    fun shouldSub() {
        val vectorA: Vector2fc = Vector2f(1f, 2f)
        val vectorB: Vector2fc = Vector2f(3f, 3f)

        Assert.assertEquals(Vector2f(-2f, -1f), vectorA - vectorB)
    }

    @Test
    fun shouldMultiply() {
        val vectorA: Vector2fc = Vector2f(0f, 2f)
        val vectorB: Vector2fc = Vector2f(3f, 3f)

        Assert.assertEquals(Vector2f(0f, 6f), vectorA * vectorB)
    }

    @Test
    fun shouldMultiplyWithScalar() {
        val vector: Vector2fc = Vector2f(0f, 2f)
        val scalar = 5f

        Assert.assertEquals(Vector2f(0f, 10f), vector * scalar)
    }

    @Test
    fun shouldUnaryMinus() {
        val vector: Vector2fc = Vector2f(5f, 2f)

        Assert.assertEquals(Vector2f(-5f, -2f), -vector)
    }

    @Test
    fun shouldMoveTowards() {
        val vector: Vector2fc = Vector2f(5f, 2f)
        val target: Vector2fc = Vector2f(10f, 2f)

        val result = vector.moveTowards(target, 2f)

        Assert.assertEquals(Vector2f(7f, 2f), result)
    }

    @Test
    fun shouldMoveTowardsClamped() {
        val vector: Vector2fc = Vector2f(5f, 2f)
        val target: Vector2fc = Vector2f(8f, 9f)

        val result = vector.moveTowards(target, 20f)

        Assert.assertEquals(Vector2f(8f, 9f), result)
    }

    @Test
    fun shouldClamp() {
        val vector: Vector2fc = Vector2f(5f, 2f)

        val result = vector.clamp(1f)

        Assert.assertEquals(Vector2f(5f, 2f).normalize(), result)
    }

    @Test
    fun shouldNotClampLonger() {
        val vector: Vector2fc = Vector2f(5f, 2f)

        val result = vector.clamp(20f)

        Assert.assertEquals(Vector2f(5f, 2f), result)
    }
}
