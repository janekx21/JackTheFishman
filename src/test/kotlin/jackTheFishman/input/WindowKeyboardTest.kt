package jackTheFishman.input

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import jackTheFishman.Window
import jackTheFishman.graphics.Texture2D
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector2i
import org.joml.Vector2ic
import org.junit.Assert
import org.junit.Rule
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.Test

internal class WindowKeyboardTest : KoinTest {

    class WindowStub : Window {
        override var shouldClose: Boolean
            get() = false
            set(value) {}
        override var physicalSize: Vector2ic
            get() = Vector2i(1280, 720)
            set(value) {}
        override val logicalSize: Vector2fc
            get() = Vector2f(1280f, 720f)
        override var contentScale: Float
            get() = 1f
            set(value) {}
        override var fullscreen: Boolean
            get() = false
            set(value) {}
        override val aspect: Float
            get() = logicalSize.x() / logicalSize.y()

        var updateEmitter: ObservableEmitter<Float>? = null
        override val onBetweenUpdates: Observable<Float>
            get() = Observable.create {
                updateEmitter = it
            }

        var keyboardEmitter: ObservableEmitter<KeyboardAction>? = null
        override val onKeyChanged: Observable<KeyboardAction>
            get() = Observable.create {
                keyboardEmitter = it
            }

        override fun setCursor(texture: Texture2D) {
            TODO("Not yet implemented")
        }

        override fun setIcon(texture: Texture2D) {
            TODO("Not yet implemented")
        }

        override fun update() {
            TODO("Not yet implemented")
        }

        override fun setCursorMode(mode: CursorMode) {
            TODO("Not yet implemented")
        }

        override val mousePosition: Vector2fc
            get() = TODO("Not yet implemented")
        override val isLeftMouseButtonDown: Boolean
            get() = TODO("Not yet implemented")
        override val isRightMouseButtonDown: Boolean
            get() = TODO("Not yet implemented")

    }

    private val myModule =
        module {
            single { WindowStub() } bind Window::class
            single { WindowKeyboard(get()) } bind Keyboard::class
        }

    private val windowStub: WindowStub by inject()
    private val keyboard: Keyboard by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(myModule)
    }


    @Test
    fun shouldStartUnset() {
        Assert.assertFalse(keyboard.down(KeyboardKey.A))
    }

    @Test
    fun `should register with flush`() {
        windowStub.keyboardEmitter!!.onNext(KeyboardAction(KeyboardKey.A, KeyboardActionType.PRESSED))
        windowStub.updateEmitter!!.onNext(.1f)

        Assert.assertTrue(keyboard.down(KeyboardKey.A))
    }

    @Test
    fun shouldNotRegisterWithoutFlush() {
        windowStub.keyboardEmitter!!.onNext(KeyboardAction(KeyboardKey.A, KeyboardActionType.PRESSED))

        Assert.assertFalse(keyboard.down(KeyboardKey.A))
    }
}
