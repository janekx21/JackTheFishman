import jackTheFishman.math.extentions.minus
import jackTheFishman.math.extentions.plus
import jackTheFishman.math.extentions.times
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
}
