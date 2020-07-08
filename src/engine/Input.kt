package engine

import Vector2fCopy
import engine.math.minus
import engine.util.DoublePointer
import org.joml.Vector2f
import org.joml.Vector2fc
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
        private var keyStates = (0..GLFW_KEY_LAST).map {
            Pair(it, ButtonState(isDown = false, changed = false))
        }.toMap()

        // Die gesammelten key-states, beim nächsten update angewendet werden.
        private var nextKeyStates = keyStates.toMutableMap()

        // Super inkonsistent mit Mouse und Controller. Egal!
        fun down(key: Int): Boolean = keyStates.getValue(key).isDown

        fun up(key: Int): Boolean = !down(key)

        fun changed(key: Int): Boolean = keyStates.getValue(key).changed

        fun justDown(key: Int): Boolean = keyStates.getValue(key).justDown

        fun justUp(key: Int): Boolean = keyStates.getValue(key).justUp

        // Wird von GLFW aufgerufen wenn sich der State von einem key geändert hat.
        fun onKeyChanged(
            key: Int,
            action: Int
        ) {
            when (action) {
                GLFW_PRESS -> {
                    nextKeyStates[key] = ButtonState(
                        isDown = true,
                        changed = !keyStates.getValue(key).isDown
                    )
                }
                GLFW_RELEASE -> {
                    nextKeyStates[key] = ButtonState(
                        isDown = false,
                        changed = keyStates.getValue(key).isDown
                    )
                }
                else -> {
                    check(action == GLFW_REPEAT)
                }
            }
        }

        // onKeyChanged verändert nichts an den Rückgabewerten von changed, justDown, justUp etc.
        // Erst wenn nach onKeyChanged die update-Funktion aufgerufen wird, werden die Änderungen wirksam gemacht.
        fun update() {
            keyStates = nextKeyStates.toMap()

            nextKeyStates = keyStates.mapValues {
                if (it.value.changed) {
                    ButtonState(isDown = it.value.isDown, changed = false)
                } else {
                    it.value
                }
            }.toMutableMap()
        }
    }

    object Mouse {
        enum class CursorMode {
            DISABLED, HIDDEN, NORMAL
        }

        var position: Vector2fc = getMousePosition()
        var previousPosition: Vector2fc = position

        var left = ButtonState(isDown = false, changed = false)
        var right = ButtonState(isDown = false, changed = false)

        fun setMode(mode: CursorMode) {
            val index = when (mode) {
                CursorMode.NORMAL -> GLFW_CURSOR_NORMAL
                CursorMode.DISABLED -> GLFW_CURSOR_DISABLED
                CursorMode.HIDDEN -> GLFW_CURSOR_HIDDEN
            }
            glfwSetInputMode(Window.pointer, GLFW_CURSOR, index)
        }

        private fun getMousePosition(): Vector2fc {
            val x = DoublePointer()
            val y = DoublePointer()

            glfwGetCursorPos(Window.pointer, x.buffer, y.buffer)
            return Vector2f(x.value.toFloat(), y.value.toFloat())
        }

        fun update() {
            previousPosition = Vector2f(position)

            position = getMousePosition()
            previousPosition = position - previousPosition

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