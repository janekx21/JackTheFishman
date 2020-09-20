package jackTheFishman.input

import jackTheFishman.BaseTest
import jackTheFishman.stubs.WindowStub
import org.junit.Assert
import org.koin.test.inject
import kotlin.test.Test

internal class WindowKeyboardTest : BaseTest() {
    private val windowStub: WindowStub by inject()
    private val keyboard: Keyboard by inject()

    @Test
    fun shouldStartUnset() {
        Assert.assertFalse(keyboard.down(KeyboardKey.A))
    }

    @Test
    fun `should register with flush`() {
        windowStub.keyboardSubject.onNext(KeyboardAction(KeyboardKey.A, KeyboardActionType.PRESSED))
        windowStub.updateSubject.onNext(.1f)

        Assert.assertTrue(keyboard.down(KeyboardKey.A))
    }

    @Test
    fun shouldNotRegisterWithoutFlush() {
        windowStub.keyboardSubject.onNext(KeyboardAction(KeyboardKey.A, KeyboardActionType.PRESSED))

        Assert.assertFalse(keyboard.down(KeyboardKey.A))
    }
}
