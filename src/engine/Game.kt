package engine

import org.joml.Vector2i
import org.lwjgl.glfw.GLFW

open class Game {

    init {
        check(GLFW.glfwInit()) { "GLFW could'nt init" }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints() // optional, the current window hints are already the default

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // the window will stay hidden after creation

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // the window will be resizable
    }

    var window = Window(Vector2i(640, 480), "FooBar")

    open fun update() {}
    open fun draw() {}

    fun run() {
        while (!window.shouldClose) {
            window.update()

            update()
            draw()
        }
    }
}