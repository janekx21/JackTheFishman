package engine

import engine.util.DoublePointer
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*

object Input {

    fun update() {
        Keyboard.update()
        Mouse.update()
    }

    object Keyboard {
        private val keyState = HashMap<Int, Boolean>()
        private val justDown = arrayListOf<Int>()

        fun down(key: Int): Boolean = keyState.getOrDefault(key, false)

        fun up(key: Int): Boolean = !down(key)

        fun click(key: Int): Boolean = justDown.contains(key)

        fun updateKeyState(
            window: Window,
            key: Int,
            scanCode: Int,
            action: Int,
            mods: Int
        ) {
            when (action) {
                GLFW_PRESS -> {
                    justDown.add(key)
                    keyState[key] = true
                }
                GLFW_RELEASE -> {
                    keyState[key] = false
                }
                else -> {
                    check(action == GLFW_REPEAT)
                }
            }
        }

        fun update() {
            justDown.clear()
        }
    }

    object Mouse {
        val position = Vector2f()
        val deltaPosition = Vector2f()
        var leftMouseButton = false

        fun setMode(mode: Int) {
            glfwSetInputMode(Window.pointer, GLFW_CURSOR, mode)
        }

        fun update() {
            deltaPosition.set(position)
            val x = DoublePointer()
            val y = DoublePointer()
            glfwGetCursorPos(Window.pointer, x.buffer, y.buffer)
            position.set(x.value.toFloat(), y.value.toFloat())
            deltaPosition.sub(position).mul(-1f)
            leftMouseButton = glfwGetMouseButton(Window.pointer, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS
        }
    }
}