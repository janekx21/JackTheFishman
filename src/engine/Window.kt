package engine

import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import java.io.Closeable


class Window(var size: Vector2i, var title: String) : Closeable {
    val shouldClose
        get() = glfwWindowShouldClose(pointer)

    private val pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)

    init {
        glfwMakeContextCurrent(pointer)

        val fov = 1.0 / 90.0

        glfwSetKeyCallback(pointer) { window, key, _, action, _ ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true)
            }
        }

        glfwSetFramebufferSizeCallback(pointer) { window, width, height ->
            size = Vector2i(width, height)
            glViewport(0, 0, width, height)
            glLoadIdentity()
            // glOrtho(-1.0, 1.0, -1.0, 1.0, -10.0, 10.0);
            val aspect = width.toFloat() / height.toFloat()
            glTranslatef(0f, 0f, -1f)
            glTranslatef(0f, -.5f, 0f);
            glMatrixMode(GL_PROJECTION)
            glLoadIdentity()
            glFrustum(-fov * aspect, fov * aspect, -fov, fov, .01, 10.0);
            glMatrixMode(GL_MODELVIEW)
        }

        glfwSwapInterval(1)
        createCapabilities()

        glEnable(GL_DEPTH_TEST)

        glfwShowWindow(pointer)
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