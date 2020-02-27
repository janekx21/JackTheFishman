import org.lwjgl.glfw.GLFW.*
import java.io.Closeable

var isInitialised = false;

fun init() {
    check(glfwInit()){"GLFW could'nt init"}
    isInitialised = true
}


class Window(var size: Point, var title: String, private var pointer: Long = 0): Closeable {
    init {
        check(isInitialised) {"is not initialed"}

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)
        glfwMakeContextCurrent(pointer)
    }

    fun open() {
        glfwShowWindow(pointer)
    }

    fun loop() {
        while(!glfwWindowShouldClose(pointer)){
            glfwPollEvents()
            glfwSwapBuffers(pointer)
        }
    }

    override fun close() {
        glfwDestroyWindow(pointer)

        // Terminate GLFW and free the error callback
        glfwTerminate()
    }
}