package jackTheFishman.engine

import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.util.FloatPointer
import jackTheFishman.engine.util.IFinalized
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import java.io.Closeable
import kotlin.math.min


/**
 * Window Wrapper that also manages the open gl context
 */
object Window : Closeable, IFinalized {
    private const val MAX_DELTA_TIME = .1f // translates to 10fps
    private const val title = "Jack the Fishman Framework"

    var size: Vector2ic = Vector2i(680, 460)

    var shouldClose = false

    val aspect: Float
        get() = size.x().toFloat() / size.y().toFloat()

    var onResize: (Window) -> Unit = {}

    val pointer = glfwCreateWindow(size.x(), size.y(), title, 0, 0)

    var contentScale: Float = 1.0F

    private var lastTime = 0.0

    private val keyCallback = GLFWKeyCallbackI { window, key, _, action, _ ->
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            close()
        }
        Input.Keyboard.onKeyChanged(key, action)
    }

    private val framebufferSizeCallback = GLFWFramebufferSizeCallbackI { _, width, height ->
        size = Vector2i(width, height)
        GL46.glViewport(0, 0, size.x(), size.y())
        onResize(this)
    }

    private val windowCloseCallback = GLFWWindowCloseCallbackI {_ ->
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
    }

    private fun configGLFW() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        glfwSwapInterval(1)

        val x = FloatPointer()
        val y = FloatPointer()
        glfwGetWindowContentScale(pointer, x.buffer, y.buffer)
        contentScale = x.value*0.5F + y.value*0.5F
    }

    private fun configOpenGL() {
        glEnable(GL_DEPTH_TEST)
    }

    private fun configEventCallbacks() {
        Legui.initializer.callbackKeeper.also {
            it.chainKeyCallback.add(keyCallback)
            it.chainFramebufferSizeCallback.add(framebufferSizeCallback)
            it.chainWindowCloseCallback.add(windowCloseCallback)
        }
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
        glfwGetTime().also {time ->
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
