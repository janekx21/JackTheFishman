package jackTheFishman

import jackTheFishman.audio.Listener
import jackTheFishman.audio.openAl.OpenAlListener
import jackTheFishman.util.Finalized
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.lwjgl.glfw.GLFW

/**
 * Base class for all games. This is the main entry point.
 */
open class Game(private val window: Window) {
    init {
        configGLFW()
        initEngineObjects()
        setupDependencyInjection()
    }

    private fun configGLFW() {
        check(GLFW.glfwInit()) { "GLFW couldn't init" }
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default
    }

    private fun initEngineObjects() {
    }

    private fun setupDependencyInjection() {
        val mainModule = module {
            single { OpenAlListener() as Listener }
        }

        startKoin {
            modules(mainModule)
        }
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
            while (!window.shouldClose) {
                window.update()
                update()

                draw()
            }
        } finally {
            Finalized.finalizeAll()
        }
    }
}
