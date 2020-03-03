import graphics.*
import linear.Location
import linear.Point
import linear.Vector
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import ui.Component
import ui.Panel
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
            if (key == GLFW_KEY_RIGHT && action == GLFW_PRESS) {
                panel2.volume += Vector.right * .1f
            }
            if (key == GLFW_KEY_LEFT && action == GLFW_PRESS) {
                panel2.volume += Vector.left * .1f
            }
        }

        glfwSetFramebufferSizeCallback(pointer) { window, width, height ->
            size = Point(width, height)
            glViewport(0, 0, width, height)
        }

        glfwSetCursorPosCallback(pointer) { window, xpos, ypos ->
            panel.position = Vector(
                xpos.toFloat() / size.x,
                1f - ypos.toFloat() / size.y
            ) * 2f - Vector.one
        }


        glfwSwapInterval(1)
        createCapabilities()
    }


    // private val font = Font("assets/fonts/consola")
    // private val text = Text(font, "Foo&Bar")

    private val panel = Panel(Vector(.1f, .1f), Vector(.5f, .2f))
    private val panel2 = Panel(Vector(.1f, .1f), Vector(.5f, .2f))
    private val panel3 = Panel(Vector(.1f, .1f), Vector(.5f, .2f))

    private val rootComponent = Component.Group.Horizontal(
        Vector.one, listOf(
            panel2,
            Component.Group.Vertical(
                Vector.one, listOf(
                    panel3,
                    Component.Final(Vector(2f, 2f))
                )
            )
        )
    )

    fun open() {
        glfwShowWindow(pointer)
    }


    fun loop() {
        // VertexBuffer(Mesh(listOf(Vertex(Vector(0f, 0f), Vector(0f, 0f)))))
        // Model(VertexBuffer(Mesh(listOf(Vertex(Vector(0f, 0f), Vector(0f, 0f))))), DefaultShader, Location.identity)

        val shader = DefaultShader
        /*
        with(glGetAttribLocation(shader.program, "position")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 4 * 4, 0)
            glEnableVertexAttribArray(this)
        }
         */

        /*
        with(glGetAttribLocation(shader.program, "uv")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 4 * 4, 4 * 2)
            glEnableVertexAttribArray(this)
        }
         */

        while (!glfwWindowShouldClose(pointer)) {
            val a = glfwGetTime()

            glClearColor(.4f, .4f, .42f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            panel.draw()
            rootComponent.draw(Vector.zero, Vector.one * 2f)
            // text.draw()

            glfwSwapBuffers(pointer)
            glfwPollEvents()

            val b = glfwGetTime()
            val fps = 1 / (b - a);
            glfwSetWindowTitle(pointer, "$title fps: ${fps.roundToInt()}")
        }
    }

    override fun close() {
        glfwDestroyWindow(pointer)

        // Terminate GLFW and free the error callback
        glfwTerminate()
    }
}