package jackTheFishman.engine

import jackTheFishman.engine.graphics.Texture2D
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import java.io.Closeable
import kotlin.math.min


/**
 * Window Wrapper that also manages the open gl context
 */
object Window : Closeable {
    private const val MAX_DELTA_TIME = .1f // translates to 10fps
    private const val title = "Jack the Fishman Framework"

    var size: Vector2ic = Vector2i(680, 460)

    var shouldClose = false

    val aspect: Float
        get() = size.x().toFloat() / size.y().toFloat()

    var onResize: (Window) -> Unit = {}

    val pointer = glfwCreateWindow(size.x(), size.y(), title, 0, 0)

    private var lastTime = 0.0

    init {
        open()
        config()
    }

    private fun open() {
        glfwMakeContextCurrent(pointer)
        createCapabilities()
        glfwShowWindow(pointer)
    }

    private fun config() {
        configGLFW()
        configEvents()
        configOpenGL()
    }

    private fun configGLFW() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        glfwSwapInterval(1)
    }

    private fun configEvents() {
        glfwSetKeyCallback(pointer) { window, key, _, action, _ ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                close()
            }
            Input.Keyboard.onKeyChanged(key, action)
        }

        glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
            size = Vector2i(width, height)
            GL46.glViewport(0, 0, size.x(), size.y())
            onResize(this)
        }

        glfwSetWindowCloseCallback(pointer) {
            close()
        }
    }

    private fun configOpenGL() {
        glEnable(GL_DEPTH_TEST)
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
        glfwDestroyWindow(pointer)
        glfwTerminate()
        shouldClose = true
    }
}
