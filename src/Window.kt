import graphics.Quad
import graphics.Texture
import math.Point
import math.Vector
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.Assimp.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
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

        glfwSetFramebufferSizeCallback(pointer) { window, width, height ->
            glViewport(0, 0, width, height)
        }

        glfwSwapInterval(1)
        createCapabilities()

        glEnable(GL_DEPTH_TEST)

        glfwShowWindow(pointer)
    }

    fun loop() {
        val scene =
            aiImportFile("assets/models/arrow.obj", aiProcess_Triangulate or aiProcess_GenNormals)
        check(scene != null) { "scene could not be loaded" }

        println("there are ${scene.mNumMeshes()} meshes")


        val l = arrayListOf<Vector>()
        val fl = arrayListOf<Float>()
        for (i in 0 until scene.mNumMeshes()) {
            val x = scene.mMeshes()?.get(i)
            if (x !== null) {
                val mesh = AIMesh.create(x)
                println("vertex len is ${mesh.mNumVertices()}")
                println("num of faces is ${mesh.mNumFaces()}")

                for (j in 0 until mesh.mNumFaces()) {
                    val face = mesh.mFaces().get(j)
                    for (k in 0 until face.mNumIndices()) {
                        val index = face.mIndices().get(k)
                        val vec = mesh.mVertices().get(index)
                        l.add(Vector(vec.x(), vec.y()))
                        fl.add(vec.x())
                        fl.add(vec.y())
                        fl.add(vec.z())
                        val normal = mesh.mNormals()?.get(index)
                        if (normal != null) {
                            fl.add(normal.x())
                            fl.add(normal.y())
                            fl.add(normal.z())
                        }
                        val uv = mesh.mTextureCoords(0)?.get(index)
                        if (uv != null) {
                            fl.add(uv.x())
                            fl.add(uv.y())
                        }
                    }
                }
            }
        }
        val quad3 = Quad(fl.toFloatArray())
        val tex = Texture("assets/textures/krakula-xl.png")
        val normal = Texture("assets/textures/normal_map.png", 1)
        // glActiveTexture(GL_TEXTURE0)
        // glActiveTexture(GL_TEXTURE1)

        val quad = Quad(floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f, 1f, 1f))
        val quad2 = Quad(floatArrayOf(-1f, -1f, -1f, -.5f, -.5f, -.5f))

        glTranslatef(0f, -.5f, 0f);

        while (!glfwWindowShouldClose(pointer)) {
            glRotatef(1f, .1f, 1f, .1567f)

            glClearColor(.2f, .2f, .2f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            tex.bind()
            normal.bind()
            quad3.draw()

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