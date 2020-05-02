package engine

import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture
import org.joml.Matrix4f
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import java.io.Closeable
import kotlin.math.sin


class Window(var size: Vector2i, var title: String, private var pointer: Long = 0) : Closeable {

    val windowPointer: Long
        get() = pointer

    val deltaTime: Float
        get() = delta

    var delta = 0f

    init {
        check(isInitialised) { "is not initialed" }

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

        pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)
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

    fun loop() {

        val loadedMesh = Mesh.createViePath("assets/models/arrow.obj")
        val tex = Texture.createViaPath("assets/textures/krakula-xl.png")
        val shader: Shader = Shader("assets/shaders/vertex.glsl", "assets/shaders/fragment.glsl")

        // val stack = Matrix4f()
        // stack.setOrtho(-1f, 1f, -1f, 1f, -1f, 1f)
        val world = Matrix4f()
        val projection = Matrix4f()


        // projection.ortho(-1f, 1.1f, -1f, 1f, -1f, 10f)

        while (!glfwWindowShouldClose(pointer)) {
            glClearColor(.2f, .2f, .2f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            val x = DoubleArray(1)
            val y = DoubleArray(1)
            glfwGetCursorPos(windowPointer, x, y)
            projection.identity()
            projection.perspective(Math.toRadians(100.0).toFloat(), 1f, .1f, 10f)

            val view = Matrix4f()
            view.translate(Vector3f(x[0].toFloat() / 100, -y[0].toFloat() / 100, -.1f))

            world.identity()
            world.translate(Vector3f(sin(glfwGetTime()).toFloat() * .1f, 0f, 0f))

            shader.setMatrix(world, view, projection)
            shader.use {
                loadedMesh.draw()
            }

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