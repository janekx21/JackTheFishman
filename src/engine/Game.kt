package engine

import engine.util.IFinalized
import org.lwjgl.glfw.GLFW

open class Game {

    init {
        check(GLFW.glfwInit()) { "GLFW could'nt init" }
        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        Window // init window
    }

    open fun update() {}
    open fun draw() {}

    fun run() {
        while (!Window.shouldClose) {
            Window.update()
            Input.update()

            update()
            draw()
        }
        IFinalized.finalizeAll()
    }
}