import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import java.io.Closeable
import kotlin.math.roundToInt

var isInitialised = false;

fun init() {
    GLFWErrorCallback.createPrint(System.err).set()
    check(glfwInit()) { "GLFW could'nt init" }
    isInitialised = true
}


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

        glfwSetFramebufferSizeCallback(pointer) { window, width, height ->
            glViewport(0, 0, width, height)
        }


        // glfwSwapInterval(1)
        createCapabilities()
    }

    fun open() {
        glfwShowWindow(pointer)
    }

    fun loop() {
        val model = Model(primitiveQuad, DefaultShader, Vector(.5f, .5f), Texture("assets/ex.png"))
        val tex = Texture("assets/ex.png")
        val quadBuffer = primitiveQuad.toVertexBuffer()
        val num = 1000;
        val arr = List<Model>(num) {i -> Model(quadBuffer, DefaultShader, Vector(i*(1f/num) - 1f, i*(1f/num) - 1f), tex)}
        var lastFps = 60f;
        while (!glfwWindowShouldClose(pointer)) {
            val a = glfwGetTime()
            glClearColor(.1f, .1f, .1f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            // model.draw()
            // quad.draw()
            for (m in arr) {
                m.draw()
            }

            glfwSwapBuffers(pointer)
            glfwPollEvents()
            val b = glfwGetTime()
            val fps = (1f / (b-a)) * .1f + lastFps*.9f;
            glfwSetWindowTitle(pointer, "fps: ${fps.roundToInt()}")
            lastFps = fps.toFloat();
        }
    }

    override fun close() {
        glfwDestroyWindow(pointer)

        // Terminate GLFW and free the error callback
        glfwTerminate()
    }
}