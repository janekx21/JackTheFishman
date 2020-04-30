import graphics.Camera
import graphics.Mesh
import graphics.Texture
import math.Point
import math.Vector
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.Assimp.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46.*
import java.io.Closeable


class Window(var size: Point, var title: String, private var pointer: Long = 0) : Closeable {

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
            size = Point(width, height)
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
        val scene =
            aiImportFile("assets/models/arrow.obj", aiProcess_Triangulate or aiProcess_GenNormals)
        check(scene != null) { "scene could not be loaded" }

        println("there are ${scene.mNumMeshes()} meshes")


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
        val quad3 = Mesh(fl.toFloatArray())
        val tex = Texture("assets/textures/krakula-xl.png")
        val normal = Texture("assets/textures/normal_map.png", 1)
        // glActiveTexture(GL_TEXTURE0)
        // glActiveTexture(GL_TEXTURE1)
        val cam = Camera(Vector(0f, 0f, -1f), 0f, 0f)

        val quad = Mesh(floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f, 1f, 1f))
        val quad2 = Mesh(floatArrayOf(-1f, -1f, -1f, -.5f, -.5f, -.5f))
        var last = 0f

        while (!glfwWindowShouldClose(pointer)) {
            // glRotatef(1f, .1f, 1f, .1567f)
            cam.update(this)
            cam.matrix(size.x.toFloat() / size.y.toFloat())

            glClearColor(.2f, .2f, .2f, 1f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            tex.bind()
            normal.bind()
            quad3.draw()

            glfwSwapBuffers(pointer)
            glfwPollEvents()
            delta = glfwGetTime().toFloat() - last
            last = glfwGetTime().toFloat()
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