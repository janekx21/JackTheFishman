package engine

import engine.util.DoublePointer
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*

object Input {

    fun update() {
        Keyboard.update()
        Mouse.update()
        Controller.update()
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


    object Controller {
        val leftStick = Vector2f(0f, 0f) // TODO: replace with Vector2fconst.zero
        val rightStick = Vector2f(0f, 0f) // TODO: "

        private val buttonArray = arrayOf<Button>(
                Button(Buttonnames.A),
                Button(Buttonnames.B),
                Button(Buttonnames.X),
                Button(Buttonnames.Y),
                Button(Buttonnames.LB),
                Button(Buttonnames.RB),
                Button(Buttonnames.BACK),
                Button(Buttonnames.START),
                Button(Buttonnames.LEFT_STICK_BUTTON),
                Button(Buttonnames.RIGHT_STICK_BUTTON),
                Button(Buttonnames.D_PAD_UP),
                Button(Buttonnames.D_PAD_RIGHT),
                Button(Buttonnames.D_PAD_DOWN),
                Button(Buttonnames.D_PAD_LEFT)
        )

        data class Button(var bname: Buttonnames) {
            var name = bname
            var pressed = false
        }

        enum class Buttonnames {
            A,
            B,
            X,
            Y,
            LB,
            RB,
            BACK,
            START,
            LEFT_STICK_BUTTON,
            RIGHT_STICK_BUTTON,
            D_PAD_UP,
            D_PAD_RIGHT,
            D_PAD_DOWN,
            D_PAD_LEFT
        }

        fun down(key: Int): Boolean {
            return buttonArray[key].pressed
        }

        fun up(key: Int): Boolean = !down(key)

        fun update() {
            if (glfwJoystickPresent(GLFW_JOYSTICK_1)) {
                // button inputs
                val inputbuttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1)!!.array()
                for (i in 0..buttonArray.size) {
                    buttonArray[i].pressed = inputbuttons[i].toInt() == GLFW_PRESS
                }
                // stick inputs
                val inputaxes = glfwGetJoystickAxes(GLFW_JOYSTICK_1)!!.array()
                leftStick.x = inputaxes[0] // TODO: Needs test! Correct implementation?
                leftStick.y = inputaxes[1]
                rightStick.x = inputaxes[2]
                rightStick.y = inputaxes[3]
            }
        }
    }

}