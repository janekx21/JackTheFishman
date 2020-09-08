package jackTheFishman.engine

import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.util.FloatPointer
import jackTheFishman.engine.util.IFinalized
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWWindowCloseCallbackI
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11.GL_BLEND
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import org.lwjgl.system.MemoryUtil
import java.io.Closeable
import kotlin.math.min


/**
 * Window Wrapper that also manages the open gl context
 */
object Window : Closeable, IFinalized {
    private const val MAX_DELTA_TIME = .1f // translates to 10fps
    private const val title = "Jack the Fishman Framework"

    var physicalSize: Vector2ic = Vector2i(1280, 720)

    var shouldClose = false

    var multiSampleCount = 1
        set(value) {
            field = value
            glfwWindowHint(GLFW_SAMPLES, value)
        }

    var fullscreen = false
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

    val aspect: Float
        get() = physicalSize.x().toFloat() / physicalSize.y().toFloat()

    var onResize: (Window) -> Unit = {}

    val pointer = glfwCreateWindow(physicalSize.x(), physicalSize.y(), title, 0, 0)

    var contentScale: Float = 1.0F

    val logicalSize: Vector2fc
        get() = Vector2f(physicalSize).div(contentScale)

    private var lastTime = 0.0

    private val keyCallback = GLFWKeyCallbackI { _, key, _, action, _ ->
        Input.Keyboard.onKeyChanged(key, action)
    }

    private val framebufferSizeCallback = GLFWFramebufferSizeCallbackI { _, width, height ->
        physicalSize = Vector2i(width, height)
        GL46.glViewport(0, 0, physicalSize.x(), physicalSize.y())
        onResize(this)
    }

    private val windowCloseCallback = GLFWWindowCloseCallbackI {
        close()
    }

    init {
        open()
        config()
    }

    private fun open() {
        glfwMakeContextCurrent(pointer)
        createCapabilities()
        glfwShowWindow(pointer)
        IFinalized.push(this)
    }

    private fun config() {
        configGLFW()
        configOpenGL()
        configEventCallbacks()
        fullscreen = false
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
        Legui.initializer.callbackKeeper.also {
            it.chainKeyCallback.add(keyCallback)
            it.chainFramebufferSizeCallback.add(framebufferSizeCallback)
            it.chainWindowCloseCallback.add(windowCloseCallback)
        }
    }

    fun setCursor(texture: Texture2D) {
        val cursor = glfwCreateCursor(texture.asGLFWImage(), 0, 0)
        if (cursor == MemoryUtil.NULL)
            throw RuntimeException("Error creating cursor")
        glfwSetCursor(pointer, cursor)
    }

    fun setIcon(texture: Texture2D) {
        val buffer = GLFWImage.malloc(1)
        buffer.put(0, texture.asGLFWImage())
        glfwSetWindowIcon(pointer, buffer)
    }

    fun update() {
        updateWindow()
        updateTime()
    }

    private fun updateWindow() {
        glfwSwapBuffers(pointer)
        glfwPollEvents()
    }

    private fun updateTime() {
        glfwGetTime().also { time ->
            // when the window should close the time jumps to 0
            if (time > 0.0) {
                val deltaTime = (time - lastTime).toFloat()
                Time.update(min(deltaTime, MAX_DELTA_TIME))
                lastTime = time
            }
        }
    }

    override fun close() {
        shouldClose = true
    }

    override fun finalize() {
        Legui.destroy()
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }
}
