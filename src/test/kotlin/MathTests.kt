import jackTheFishman.math.extentions.plus
import org.joml.Vector2f
import org.joml.Vector2fc
import org.junit.Assert
import kotlin.test.Test

class MathTests {
    @Test
    fun testVectorAddition() {
        val vectorA: Vector2fc = Vector2f(1f, 2f)
        val vectorB: Vector2fc = Vector2f(3f, 3f)

        Assert.assertEquals(vectorA + vectorB, Vector2f(3f, 6f))
    }
}
