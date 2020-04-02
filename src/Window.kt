
import graphics.Camera
import graphics.Framebuffer
import graphics.Mesh
import graphics.Texture
import math.Point2
import math.Vector3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import java.io.Closeable


object Window : Closeable {
    var size: Point2 = Point2(640, 480)
    private var title: String = "fooBar"

    init {
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "GLFW could'nt init" }

        // Configure GLFW
        glfwDefaultWindowHints() // optional, the current window hints are already the default

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
    }

    val pointer = glfwCreateWindow(size.x, size.y, title, 0, 0)

    init {
        glfwMakeContextCurrent(pointer)

        val fov = 1.0 / 90.0

        glfwSetKeyCallback(pointer) { window, key, scanCode, action, mods ->
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true)
            }
            Input.Keyboard.updateKeyState(this, key, scanCode, action, mods)
        }

        glfwSetFramebufferSizeCallback(pointer) { _, width, height ->
            size = Point2(width, height)
            glViewport(0, 0, width, height)
            glLoadIdentity()
            val aspect = width.toFloat() / height.toFloat()
            glTranslatef(0f, 0f, -1f)
            glTranslatef(0f, -.5f, 0f)
            glMatrixMode(GL_PROJECTION)
            glLoadIdentity()
            glFrustum(-fov * aspect, fov * aspect, -fov, fov, .01, 10.0)
            glMatrixMode(GL_MODELVIEW)
        }

        glfwSwapInterval(1)
        createCapabilities()

        glEnable(GL_DEPTH_TEST)

        glfwShowWindow(pointer)
    }

    fun loop() {
        val quad3 = Mesh("assets/models/arrow.obj")
        val tex = Texture.fromPath("assets/textures/krakula-xl.png")
        val normal = Texture.fromPath("assets/textures/normal_map.png")
        val cam = Camera(Vector3(0f, 0f, -1f), 0f, 0f)

        val quad = Mesh(
            floatArrayOf(
                -1f, -1f, 0f, 0f, 0f, -1f, 0f, 0f,
                +1f, -1f, 0f, 0f, 0f, -1f, 1f, 0f,
                +1f, +1f, 0f, 0f, 0f, -1f, 1f, 1f,

                -1f, -1f, 0f, 0f, 0f, -1f, 0f, 0f,
                +1f, +1f, 0f, 0f, 0f, -1f, 1f, 1f,
                -1f, +1f, 0f, 0f, 0f, -1f, 0f, 1f
            )
        )

        val cameraFrameBuffer = Framebuffer()
        val cameraTexture = Texture(size)

        cameraFrameBuffer.bind {
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, cameraTexture.texture, 0)

            with(glGenTextures()) {
                glBindTexture(GL_TEXTURE_2D, this)
                glTexImage2D(
                    GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, size.x, size.y, 0,
                    GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, 0
                )
                glBindTexture(GL_TEXTURE_2D, 0)
                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, this, 0)
            }
        }

        while (!glfwWindowShouldClose(pointer)) {
            glfwPollEvents()
            Input.Keyboard.update()
            Input.Mouse.update()
            Time.update(glfwGetTime().toFloat())

            cam.update()
            cam.matrix(size.x.toFloat() / size.y.toFloat())

            cameraFrameBuffer.bind {
                glClearColor(1f, 1f, 1f, 1f)
                glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
                check(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
                    "Framebuffer status not complete"
                }

                tex.bind(0) {
                    normal.bind(1) {
                        quad3.draw()
                    }
                }
            }

            glClearColor(.2f, .2f, .2f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            glLoadIdentity()
            glTranslatef(0f, 0f, -1f)
            cameraTexture.bind(0) {
                normal.bind(1) {
                    quad.draw()
                }
            }

            glBindTexture(GL_TEXTURE_2D, 0)

            glfwSwapBuffers(pointer)
        }
    }

    override fun close() {
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }
}