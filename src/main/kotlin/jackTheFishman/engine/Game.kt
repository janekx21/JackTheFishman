package jackTheFishman.engine

import jackTheFishman.engine.util.IFinalized
import org.lwjgl.glfw.GLFW

open class Game {
    init {
        check(GLFW.glfwInit()) { "GLFW could'nt init" }
        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
        //GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 2)
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