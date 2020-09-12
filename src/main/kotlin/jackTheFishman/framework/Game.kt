package jackTheFishman.framework

import jackTheFishman.framework.util.Finalized
import org.lwjgl.glfw.GLFW

/**
 * Base class for all games. This is the main entry point.
 */
open class Game {
    init {
        configGLFW()
        initEngineObjects()
    }

    private fun configGLFW() {
        check(GLFW.glfwInit()) { "GLFW could'nt init" }
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
    }

    private fun initEngineObjects() {
        Window
        Physics
    }

    /**
     * Updates the game
     */
    open fun update() {}

    /**
     * Draws the game
     */
    open fun draw() {}

    /**
     * Runs the game. Blocks execution while the game runs.
     */
    fun run() {
        try {
            while (!Window.shouldClose) {
                Window.update()
                Legui.update()
                Input.update()
                Physics.update()

                update()
                draw()
                Legui.draw()
            }
        } finally {
            Finalized.finalizeAll()
        }
    }
}
