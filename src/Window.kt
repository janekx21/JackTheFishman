import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import java.io.Closeable


class Window(var size: Point, var title: String, private var pointer: Long = 0) : Closeable {
    init {
        check(isInitialised) { "is not initialed" }

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)
        glfwMakeContextCurrent(pointer)

        glfwSetKeyCallback(pointer) { window, key, scancode, action, mods ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true)
            }
        }

        glfwSwapInterval(1)
        createCapabilities()

        glfwShowWindow(pointer)
    }

    fun loop() {
        val quad = Quad(floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f, 1f, 1f))
        val quad2 = Quad(floatArrayOf(-1f, -1f, -1f, -.5f, -.5f, -.5f))
        while (!glfwWindowShouldClose(pointer)) {
            glClearColor(.2f, .2f, .2f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            quad.draw()
            quad2.draw()

            glfwSwapBuffers(pointer)
            glfwPollEvents()
        }
    }

    override fun close() {
        glfwDestroyWindow(pointer)
    }

    companion object {
        var isInitialised = false;

        fun init() {
            GLFWErrorCallback.createPrint(System.err).set()
            check(glfwInit()) { "GLFW could'nt init" }
            isInitialised = true
        }

        fun close() {
            // Terminate GLFW and free the error callback
            glfwTerminate()
        }
    }
}