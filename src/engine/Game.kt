package engine

import engine.util.IFinalized
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_SAMPLES

open class Game {
    init {
        check(GLFW.glfwInit()) { "GLFW could'nt init" }
        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW_SAMPLES, 4) // antialiasing
        Window // init window
        Audio
        Physics
    }

    open fun update() {}
    open fun draw() {}

    fun run() {
        try {
            while (!Window.shouldClose) {
                Window.update()
                Input.update()
            Physics.update()

                update()
                draw()
            }
        } finally {
            IFinalized.finalizeAll()
        }
    }
}