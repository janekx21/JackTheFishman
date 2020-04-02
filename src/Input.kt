import math.Vector2
import org.lwjgl.glfw.GLFW.*

object Input {
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
        var position = Vector2.zero
        var deltaPosition = Vector2.zero

        fun setMode(mode: Int) {
            glfwSetInputMode(Window.pointer, GLFW_CURSOR, mode)
        }

        fun update() {
            val last = position.copy()
            val x = doubleArrayOf(0.0)
            val y = doubleArrayOf(0.0)
            glfwGetCursorPos(Window.pointer, x, y)
            position = Vector2(x[0].toFloat(), y[0].toFloat())
            deltaPosition = position - last
        }
    }
}