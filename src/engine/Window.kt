package engine

import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import java.io.Closeable


class Window(var size: Vector2i, var title: String) : Closeable {
    val shouldClose
        get() = glfwWindowShouldClose(pointer)
    val fov = 1.0 / 90.0
    val aspect: Float
        get() = size.x.toFloat() / size.y.toFloat()
    var onResize: (Window) -> Unit = {}

    private val pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)

    init {
        config()
        glfwMakeContextCurrent(pointer)
        createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glfwShowWindow(pointer)
    }

    private fun config() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        configEvents()
        glfwSwapInterval(1)
    }

    private fun configEvents() {
        glfwSetKeyCallback(pointer) { window, key, _, action, _ ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true)
            }
        }

        glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
            size = Vector2i(width, height)
            onResize(this)
        }

    }

    fun update() {
        glfwSwapBuffers(pointer)
        glfwPollEvents()
    }

    override fun close() {
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }
}