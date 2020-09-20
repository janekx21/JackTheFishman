package jackTheFishman.input

import jackTheFishman.BaseTest
import jackTheFishman.stubs.WindowStub
import org.joml.Vector2f
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals


internal class WindowMouseTest : BaseTest() {
    private val windowStub: WindowStub by inject()
    private val mouse: Mouse by inject()

    @Test
    fun `should return the right position after flush`() {
        windowStub.mousePosition = Vector2f(32f, 54f)

        windowStub.updateSubject.onNext(.1f)

        assertEquals(Vector2f(32f, 54f), mouse.position)
    }

    @Test
    fun `should return the wrong position without flush`() {
        val defaultPosition = windowStub.mousePosition
        windowStub.mousePosition = Vector2f(32f, 54f)

        assertEquals(defaultPosition, mouse.position)
    }
}
