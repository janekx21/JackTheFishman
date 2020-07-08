package engine

import engine.graphics.Texture2D
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import java.io.Closeable


object Window : Closeable {
    var size: Vector2ic = Vector2i(680, 460)
    private const val title = "Jack the Fishman Framework"
    val shouldClose
        get() = glfwWindowShouldClose(pointer)
    val aspect: Float
        get() = size.x().toFloat() / size.y().toFloat()
    var onResize: (Window) -> Unit = {}
    val pointer = glfwCreateWindow(size.x(), size.y(), title, 0, 0)

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
        glfwSetKeyCallback(pointer) { window, key, _, action, _ ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true)
            }
            Input.Keyboard.onKeyChanged(key, action)
        }

        glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
            size = Vector2i(width, height)
            GL46.glViewport(0, 0, size.x(), size.y())
            onResize(this)
        }

    }

    fun setIcon(texture: Texture2D) {
        val buffer = GLFWImage.malloc(1)
        buffer.put(0, texture.toGLFWImage())
        glfwSetWindowIcon(pointer, buffer)
    }

    fun update() {
        val time = glfwGetTime()
        glfwSwapBuffers(pointer)
        glfwPollEvents()
        Time.update((time - lastTime).toFloat())
        lastTime = time
    }

    override fun close() {
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }
}