package jackTheFishman

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import jackTheFishman.graphics.Texture2D
import jackTheFishman.input.CursorMode
import jackTheFishman.input.KeyboardAction
import jackTheFishman.input.KeyboardActionType
import jackTheFishman.input.KeyboardKey
import jackTheFishman.util.Finalized
import jackTheFishman.util.pointer.DoublePointer
import jackTheFishman.util.pointer.FloatPointer
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.glfw.GLFWWindowCloseCallbackI
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import org.lwjgl.system.MemoryUtil
import java.io.Closeable


/**
 * Window Wrapper that also manages the open gl context
 */
class GlfwWindow : Window, Closeable, Finalized {
    override var physicalSize: Vector2ic = Vector2i(1280, 720)
    override var shouldClose = false
    override val mousePosition: Vector2fc
        get() {
            val x = DoublePointer()
            val y = DoublePointer()
            glfwGetCursorPos(pointer, x.array, y.array)
            return Vector2f(x.value.toFloat(), y.value.toFloat())
        }
    override val isLeftMouseButtonDown: Boolean
        get() = glfwGetMouseButton(pointer, GLFW_MOUSE_BUTTON_LEFT) != GLFW_RELEASE
    override val isRightMouseButtonDown: Boolean
        get() = glfwGetMouseButton(pointer, GLFW_MOUSE_BUTTON_RIGHT) != GLFW_RELEASE


    var multiSampleCount = 1
        set(value) {
            field = value
            glfwWindowHint(GLFW_SAMPLES, value)
        }

    override var fullscreen = false
        set(value) {
            if (value) {
                val mode = glfwGetVideoMode(glfwGetPrimaryMonitor())
                checkNotNull(mode)
                glfwSetWindowMonitor(
                    pointer,
                    glfwGetPrimaryMonitor(),
                    0,
                    0,
                    mode.width(),
                    mode.height(),
                    GLFW_DONT_CARE
                )
            } else {
                physicalSize = Vector2i(1280, 720)
                glfwSetWindowMonitor(pointer, 0, 0, 25, physicalSize.x(), physicalSize.y(), GLFW_DONT_CARE)
            }
            field = value
        }

    override val aspect: Float
        get() = physicalSize.x().toFloat() / physicalSize.y().toFloat()

    var onResize: (GlfwWindow) -> Unit = {}

    val pointer = glfwCreateWindow(physicalSize.x(), physicalSize.y(), Companion.title, 0, 0)

    override var contentScale: Float = 1.0f

    override val logicalSize: Vector2fc
        get() = Vector2f(physicalSize).div(contentScale)

    private val framebufferSizeCallback = GLFWFramebufferSizeCallbackI { _, width, height ->
        physicalSize = Vector2i(width, height)
        GL46.glViewport(0, 0, physicalSize.x(), physicalSize.y())
        onResize(this)
    }

    private val windowCloseCallback = GLFWWindowCloseCallbackI {
        close()
    }

    private var updatesEmitter: ObservableEmitter<Float>? = null
    override val onBetweenUpdates: Observable<Float> = Observable.create { updatesEmitter = it }

    private var keyChangedEmitter: ObservableEmitter<KeyboardAction>? = null
    override val onKeyChanged: Observable<KeyboardAction> = Observable.create { }

    init {
        open()
        config()
    }

    private fun open() {
        glfwMakeContextCurrent(pointer)
        createCapabilities()
        glfwShowWindow(pointer)
        Finalized.push(this)
    }

    private fun config() {
        configGLFW()
        configOpenGL()
        configEventCallbacks()
    }

    private fun configGLFW() {
        glfwSetWindowSizeLimits(pointer, 1280, 720, GLFW_DONT_CARE, GLFW_DONT_CARE) // set only the minimum window size
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        multiSampleCount = 4
        glfwSwapInterval(1)

        val x = FloatPointer()
        val y = FloatPointer()
        glfwGetWindowContentScale(pointer, x.buffer, y.buffer)
        contentScale = x.value * 0.5F + y.value * 0.5F
    }

    private fun configOpenGL() {
        glEnable(GL_BLEND)
        glEnable(GL_DEPTH_TEST)
    }

    private fun configEventCallbacks() {
    }

    private fun emitKeyAction(key: Int, action: Int) {
        val actionType = when (action) {
            GLFW_PRESS -> KeyboardActionType.PRESSED
            GLFW_RELEASE -> KeyboardActionType.RELEASED
            else -> error("Keyboard action not supported")
        }

        if (glfwKeyToKey.containsKey(key)) {
            keyChangedEmitter?.onNext(KeyboardAction(glfwKeyToKey[key] ?: error("Key not found"), actionType))
        }
    }


    override fun setCursor(texture: Texture2D) {
        val cursor = glfwCreateCursor(texture.asGLFWImage(), 0, 0)
        if (cursor == MemoryUtil.NULL)
            throw RuntimeException("Error creating cursor")
        glfwSetCursor(pointer, cursor)
    }

    override fun setIcon(texture: Texture2D) {
        val buffer = GLFWImage.malloc(1)
        buffer.put(0, texture.asGLFWImage())
        glfwSetWindowIcon(pointer, buffer)
    }

    override fun update() {
        updateWindow()
        updateTime()
    }

    override fun setCursorMode(mode: CursorMode) {
        val glfwCursorMode = when (mode) {
            CursorMode.NORMAL -> GLFW_CURSOR_NORMAL
            CursorMode.DISABLED -> GLFW_CURSOR_DISABLED
            CursorMode.HIDDEN -> GLFW_CURSOR_HIDDEN
        }
        glfwSetInputMode(pointer, GLFW_CURSOR, glfwCursorMode)
    }

    private fun updateWindow() {
        glfwSwapBuffers(pointer)
        glfwPollEvents()
    }

    private fun updateTime() {
        val time = glfwGetTime()
        if (isValidTime(time)) {
            updatesEmitter?.onNext(time.toFloat())
        }
    }

    private fun isValidTime(time: Double): Boolean {
        // When the window should close the time jumps to 0
        return time > 0f
    }

    override fun close() {
        shouldClose = true
    }

    override fun finalize() {
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }

    companion object {
        private const val title = "Jack the Fishman Framework"

        private val glfwKeyToKey = mapOf(
            GLFW_KEY_SPACE to KeyboardKey.SPACE,
            GLFW_KEY_APOSTROPHE to KeyboardKey.APOSTROPHE,
            GLFW_KEY_COMMA to KeyboardKey.COMMA,
            GLFW_KEY_MINUS to KeyboardKey.MINUS,
            GLFW_KEY_PERIOD to KeyboardKey.PERIOD,
            GLFW_KEY_SLASH to KeyboardKey.SLASH,
            GLFW_KEY_0 to KeyboardKey.NUMBER_0,
            GLFW_KEY_1 to KeyboardKey.NUMBER_1,
            GLFW_KEY_2 to KeyboardKey.NUMBER_2,
            GLFW_KEY_3 to KeyboardKey.NUMBER_3,
            GLFW_KEY_4 to KeyboardKey.NUMBER_4,
            GLFW_KEY_5 to KeyboardKey.NUMBER_5,
            GLFW_KEY_6 to KeyboardKey.NUMBER_6,
            GLFW_KEY_7 to KeyboardKey.NUMBER_7,
            GLFW_KEY_8 to KeyboardKey.NUMBER_8,
            GLFW_KEY_9 to KeyboardKey.NUMBER_9,
            GLFW_KEY_SEMICOLON to KeyboardKey.SEMICOLON,
            GLFW_KEY_EQUAL to KeyboardKey.EQUAL,
            GLFW_KEY_A to KeyboardKey.A,
            GLFW_KEY_B to KeyboardKey.B,
            GLFW_KEY_C to KeyboardKey.C,
            GLFW_KEY_D to KeyboardKey.D,
            GLFW_KEY_E to KeyboardKey.E,
            GLFW_KEY_F to KeyboardKey.F,
            GLFW_KEY_G to KeyboardKey.G,
            GLFW_KEY_H to KeyboardKey.H,
            GLFW_KEY_I to KeyboardKey.I,
            GLFW_KEY_J to KeyboardKey.J,
            GLFW_KEY_K to KeyboardKey.K,
            GLFW_KEY_L to KeyboardKey.L,
            GLFW_KEY_M to KeyboardKey.M,
            GLFW_KEY_N to KeyboardKey.N,
            GLFW_KEY_O to KeyboardKey.O,
            GLFW_KEY_P to KeyboardKey.P,
            GLFW_KEY_Q to KeyboardKey.Q,
            GLFW_KEY_R to KeyboardKey.R,
            GLFW_KEY_S to KeyboardKey.S,
            GLFW_KEY_T to KeyboardKey.T,
            GLFW_KEY_U to KeyboardKey.U,
            GLFW_KEY_V to KeyboardKey.V,
            GLFW_KEY_W to KeyboardKey.W,
            GLFW_KEY_X to KeyboardKey.X,
            GLFW_KEY_Y to KeyboardKey.Y,
            GLFW_KEY_Z to KeyboardKey.Z,
            GLFW_KEY_LEFT_BRACKET to KeyboardKey.LEFT_BRACKET,
            GLFW_KEY_BACKSLASH to KeyboardKey.BACKSLASH,
            GLFW_KEY_RIGHT_BRACKET to KeyboardKey.RIGHT_BRACKET,
            GLFW_KEY_GRAVE_ACCENT to KeyboardKey.GRAVE_ACCENT,
            GLFW_KEY_WORLD_1 to KeyboardKey.WORLD_1,
            GLFW_KEY_WORLD_2 to KeyboardKey.WORLD_2,
            GLFW_KEY_ESCAPE to KeyboardKey.ESCAPE,
            GLFW_KEY_ENTER to KeyboardKey.ENTER,
            GLFW_KEY_TAB to KeyboardKey.TAB,
            GLFW_KEY_BACKSPACE to KeyboardKey.BACKSPACE,
            GLFW_KEY_INSERT to KeyboardKey.INSERT,
            GLFW_KEY_DELETE to KeyboardKey.DELETE,
            GLFW_KEY_RIGHT to KeyboardKey.RIGHT,
            GLFW_KEY_LEFT to KeyboardKey.LEFT,
            GLFW_KEY_DOWN to KeyboardKey.DOWN,
            GLFW_KEY_UP to KeyboardKey.UP,
            GLFW_KEY_PAGE_UP to KeyboardKey.PAGE_UP,
            GLFW_KEY_PAGE_DOWN to KeyboardKey.PAGE_DOWN,
            GLFW_KEY_HOME to KeyboardKey.HOME,
            GLFW_KEY_END to KeyboardKey.END,
            GLFW_KEY_CAPS_LOCK to KeyboardKey.CAPS_LOCK,
            GLFW_KEY_SCROLL_LOCK to KeyboardKey.SCROLL_LOCK,
            GLFW_KEY_NUM_LOCK to KeyboardKey.NUM_LOCK,
            GLFW_KEY_PRINT_SCREEN to KeyboardKey.PRINT_SCREEN,
            GLFW_KEY_PAUSE to KeyboardKey.PAUSE,
            GLFW_KEY_F1 to KeyboardKey.F1,
            GLFW_KEY_F2 to KeyboardKey.F2,
            GLFW_KEY_F3 to KeyboardKey.F3,
            GLFW_KEY_F4 to KeyboardKey.F4,
            GLFW_KEY_F5 to KeyboardKey.F5,
            GLFW_KEY_F6 to KeyboardKey.F6,
            GLFW_KEY_F7 to KeyboardKey.F7,
            GLFW_KEY_F8 to KeyboardKey.F8,
            GLFW_KEY_F9 to KeyboardKey.F9,
            GLFW_KEY_F10 to KeyboardKey.F10,
            GLFW_KEY_F11 to KeyboardKey.F11,
            GLFW_KEY_F12 to KeyboardKey.F12,
            GLFW_KEY_F13 to KeyboardKey.F13,
            GLFW_KEY_F14 to KeyboardKey.F14,
            GLFW_KEY_F15 to KeyboardKey.F15,
            GLFW_KEY_F16 to KeyboardKey.F16,
            GLFW_KEY_F17 to KeyboardKey.F17,
            GLFW_KEY_F18 to KeyboardKey.F18,
            GLFW_KEY_F19 to KeyboardKey.F19,
            GLFW_KEY_F20 to KeyboardKey.F20,
            GLFW_KEY_F21 to KeyboardKey.F21,
            GLFW_KEY_F22 to KeyboardKey.F22,
            GLFW_KEY_F23 to KeyboardKey.F23,
            GLFW_KEY_F24 to KeyboardKey.F24,
            GLFW_KEY_F25 to KeyboardKey.F25,
            GLFW_KEY_KP_0 to KeyboardKey.KEYPAD_0,
            GLFW_KEY_KP_1 to KeyboardKey.KEYPAD_1,
            GLFW_KEY_KP_2 to KeyboardKey.KEYPAD_2,
            GLFW_KEY_KP_3 to KeyboardKey.KEYPAD_3,
            GLFW_KEY_KP_4 to KeyboardKey.KEYPAD_4,
            GLFW_KEY_KP_5 to KeyboardKey.KEYPAD_5,
            GLFW_KEY_KP_6 to KeyboardKey.KEYPAD_6,
            GLFW_KEY_KP_7 to KeyboardKey.KEYPAD_7,
            GLFW_KEY_KP_8 to KeyboardKey.KEYPAD_8,
            GLFW_KEY_KP_9 to KeyboardKey.KEYPAD_9,
            GLFW_KEY_KP_DECIMAL to KeyboardKey.KEYPAD_DECIMAL,
            GLFW_KEY_KP_DIVIDE to KeyboardKey.KEYPAD_DIVIDE,
            GLFW_KEY_KP_MULTIPLY to KeyboardKey.KEYPAD_MULTIPLY,
            GLFW_KEY_KP_SUBTRACT to KeyboardKey.KEYPAD_SUBTRACT,
            GLFW_KEY_KP_ADD to KeyboardKey.KEYPAD_ADD,
            GLFW_KEY_KP_ENTER to KeyboardKey.KEYPAD_ENTER,
            GLFW_KEY_KP_EQUAL to KeyboardKey.KEYPAD_EQUAL,
            GLFW_KEY_LEFT_SHIFT to KeyboardKey.LEFT_SHIFT,
            GLFW_KEY_LEFT_CONTROL to KeyboardKey.LEFT_CONTROL,
            GLFW_KEY_LEFT_ALT to KeyboardKey.LEFT_ALT,
            GLFW_KEY_LEFT_SUPER to KeyboardKey.LEFT_SUPER,
            GLFW_KEY_RIGHT_SHIFT to KeyboardKey.RIGHT_SHIFT,
            GLFW_KEY_RIGHT_CONTROL to KeyboardKey.RIGHT_CONTROL,
            GLFW_KEY_RIGHT_ALT to KeyboardKey.RIGHT_ALT,
            GLFW_KEY_RIGHT_SUPER to KeyboardKey.RIGHT_SUPER,
            GLFW_KEY_MENU to KeyboardKey.MENU
        )
    }
}