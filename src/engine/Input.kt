package engine

import Vector2fCopy
import engine.util.DoublePointer
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWGamepadState

object Input {

    data class ButtonState(val isDown: Boolean, val changed: Boolean) {
        val justDown = isDown && changed
        val justUp = !isDown && changed
    }

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
        var position = Vector2fCopy.zero
        var deltaPosition = Vector2fCopy.zero

        var left = ButtonState(isDown = false, changed = false)
        var right = ButtonState(isDown = false, changed = false)

        fun setMode(mode: Int) {
            glfwSetInputMode(Window.pointer, GLFW_CURSOR, mode)
        }

        fun update() {
            deltaPosition = Vector2f(position)

            val x = DoublePointer()
            val y = DoublePointer()

            glfwGetCursorPos(Window.pointer, x.buffer, y.buffer)

            position = Vector2f(x.value.toFloat(), y.value.toFloat())
            deltaPosition.sub(position).mul(-1f)

            val leftDown = glfwGetMouseButton(Window.pointer, GLFW_MOUSE_BUTTON_LEFT) != GLFW_RELEASE
            val rightDown = glfwGetMouseButton(Window.pointer, GLFW_MOUSE_BUTTON_RIGHT) != GLFW_RELEASE

            left = ButtonState(isDown = leftDown, changed = leftDown != left.isDown)
            right = ButtonState(isDown = rightDown, changed = rightDown != right.isDown)
        }
    }

    object Controller {
        enum class Button(val glfwKeyCode: Int) {
            A(GLFW_GAMEPAD_BUTTON_A),
            B(GLFW_GAMEPAD_BUTTON_B),
            X(GLFW_GAMEPAD_BUTTON_X),
            Y(GLFW_GAMEPAD_BUTTON_Y),
            LB(GLFW_GAMEPAD_BUTTON_LEFT_BUMPER),
            RB(GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER),
            BACK(GLFW_GAMEPAD_BUTTON_BACK),
            START(GLFW_GAMEPAD_BUTTON_START),
            //LEFT_STICK_BUTTON(),
            //RIGHT_STICK_BUTTON(),
            D_PAD_UP(GLFW_GAMEPAD_BUTTON_DPAD_UP),
            D_PAD_RIGHT(GLFW_GAMEPAD_BUTTON_DPAD_RIGHT),
            D_PAD_DOWN(GLFW_GAMEPAD_BUTTON_DPAD_DOWN),
            D_PAD_LEFT(GLFW_GAMEPAD_BUTTON_DPAD_LEFT)
        }

        var leftStick = Vector2fCopy.zero

        var rightStick = Vector2fCopy.zero

        // der button state, der allen buttons beim konstruieren gegeben wird
        private val initialButtonState = ButtonState(isDown = false, changed = false)

        // die button states der einzelnen buttons nach dem letzten update
        private val buttonStates = enumValues<Button>()
            .map { Pair(it, initialButtonState) }
            .toMap().toMutableMap()


        fun isDown(button: Button): Boolean {
            return buttonStates.getValue(button).isDown
        }

        fun isUp(button: Button): Boolean {
            return !isDown(button)
        }

        fun didButtonChange(button: Button): Boolean {
            return buttonStates.getValue(button).changed
        }

        fun justDown(button: Button): Boolean = buttonStates.getValue(button).justDown

        fun justUp(button: Button): Boolean = buttonStates.getValue(button).justUp

        fun update() {
            if (glfwJoystickPresent(GLFW_JOYSTICK_1) && glfwJoystickIsGamepad(GLFW_JOYSTICK_1)) {
                val state = GLFWGamepadState.create()
                glfwGetGamepadState(GLFW_JOYSTICK_1, state)

                // Button Inputs
                buttonStates.keys.forEach {
                    val before = isDown(it)
                    val now = state.buttons(it.glfwKeyCode).toInt() != GLFW_RELEASE

                    buttonStates[it] = ButtonState(
                        isDown = now,
                        changed = before != now
                    )
                }

                // Stick Inputs
                leftStick = Vector2f(
                    state.axes(GLFW_GAMEPAD_AXIS_LEFT_X),
                    state.axes(GLFW_GAMEPAD_AXIS_LEFT_Y)
                )

                rightStick = Vector2f(
                    state.axes(GLFW_GAMEPAD_AXIS_RIGHT_X),
                    state.axes(GLFW_GAMEPAD_AXIS_RIGHT_Y)
                )
            } else {
                // wenn kein Controller angeschlossen ist bzw. der Controller getrennt wurde,
                // ist das so als würden wir null-input vom Controller erhalten, also
                // keine Knöpfe gedrückt (changed wird entsprechend gesetzt), und die Joysticks bei (0, 0)
                buttonStates.keys.forEach {
                    buttonStates[it] = ButtonState(
                        isDown = false,
                        changed = buttonStates[it]!!.isDown != false
                    )
                }

                leftStick = Vector2fCopy.zero
                rightStick = Vector2fCopy.zero
            }
        }
    }

}