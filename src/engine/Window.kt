package engine

import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import java.io.Closeable


object Window : Closeable {
    val size = Vector2i(680, 460)
    val title = "FooBar"
    val shouldClose
        get() = glfwWindowShouldClose(pointer)
    val fov = 1.0 / 90.0
    val aspect: Float
        get() = size.x.toFloat() / size.y.toFloat()
    var onResize: (Window) -> Unit = {}
    val pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)

    private var lastTime = 0.0

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
        glfwSetKeyCallback(pointer) { window, key, scanCode, action, mods ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true)
            }
            Input.Keyboard.updateKeyState(this, key, scanCode, action, mods)
        }

        glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
            size.set(width, height)
            onResize(this)
        }

    }

    fun update() {
        val time = glfwGetTime()
        glfwSwapBuffers(pointer)
        glfwPollEvents()
        Time.update((lastTime - time).toFloat())
        lastTime = time
    }

    override fun close() {
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }
}