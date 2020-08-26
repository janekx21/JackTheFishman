package jackTheFishman.engine

import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.util.IFinalized
import org.joml.Vector2i
import org.joml.Vector2ic
import org.liquidengine.legui.DefaultInitializer
import org.liquidengine.legui.component.Frame
import org.liquidengine.legui.system.layout.LayoutManager
import org.lwjgl.glfw.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_DEPTH_TEST
import org.lwjgl.opengl.GL46.glEnable
import java.io.Closeable
import java.nio.FloatBuffer
import kotlin.math.min


/**
 * Window Wrapper that also manages the open gl context
 */
object Window : Closeable, IFinalized {
    private const val MAX_DELTA_TIME = .1f // translates to 10fps
    private const val title = "Jack the Fishman Framework"

    var size: Vector2ic = Vector2i(680, 460)

    var shouldClose = false

    val aspect: Float
        get() = size.x().toFloat() / size.y().toFloat()

    var onResize: (Window) -> Unit = {}

    val pointer = glfwCreateWindow(size.x(), size.y(), title, 0, 0)

    var contentScale: Float = 1.0F

    private var lastTime = 0.0

    lateinit var leguiFrame: Frame

    lateinit var leguiInitializer: DefaultInitializer

    private val keyCallback = GLFWKeyCallbackI { window, key, _, action, _ ->
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
            close()
        }
        Input.Keyboard.onKeyChanged(key, action)
    }

    private val framebufferSizeCallback = GLFWFramebufferSizeCallbackI { _, width, height ->
        size = Vector2i(width, height)
        GL46.glViewport(0, 0, size.x(), size.y())
        onResize(this)
    }

    private val windowCloseCallback = GLFWWindowCloseCallbackI {_ ->
        close()
    }

    init {
        open()
        config()
    }

    private fun open() {
        glfwMakeContextCurrent(pointer)
        createCapabilities()
        glfwShowWindow(pointer)

        leguiFrame = Frame(size.x().toFloat(), size.y().toFloat())
        leguiInitializer = DefaultInitializer(pointer, leguiFrame)
        leguiInitializer.renderer.initialize()

        leguiInitializer.callbackKeeper.also {
            it.chainKeyCallback.add(keyCallback)
            it.chainFramebufferSizeCallback.add(framebufferSizeCallback)
            it.chainWindowCloseCallback.add(windowCloseCallback)
        }

        IFinalized.push(this)
    }

    private fun config() {
        configGLFW()
        configOpenGL()
    }

    private fun configGLFW() {
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable
        glfwSwapInterval(1)

        val x = FloatArray(1)
        val y = FloatArray(1)
        glfwGetWindowContentScale(pointer, x, y)
        contentScale = x[0]*0.5F + y[0]*0.5F
    }

    private fun configOpenGL() {
        glEnable(GL_DEPTH_TEST)
    }

    fun setIcon(texture: Texture2D) {
        val buffer = GLFWImage.malloc(1)
        buffer.put(0, texture.asGLFWImage())
        glfwSetWindowIcon(pointer, buffer)
    }

    fun update() {
        updateWindow()
        updateTime()
    }

    private fun updateWindow() {
        glfwSwapBuffers(pointer)
        glfwPollEvents()
        leguiInitializer.systemEventProcessor.processEvents(leguiFrame, leguiInitializer.context)
        leguiInitializer.guiEventProcessor.processEvents()
    }

    private fun updateTime() {
        glfwGetTime().also {time ->
            // when the window should close the time jumps to 0
            if (time > 0.0) {
                val deltaTime = (time - lastTime).toFloat()
                Time.update(min(deltaTime, MAX_DELTA_TIME))
                lastTime = time
            }
        }
    }

    fun draw() {
        leguiInitializer.context.updateGlfwWindow()
        LayoutManager.getInstance().layout(leguiFrame)
        leguiInitializer.renderer.render(leguiFrame, leguiInitializer.context)
    }

    override fun close() {
        shouldClose = true
    }

    override fun finalize() {
        leguiInitializer.renderer.destroy()
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }
}
