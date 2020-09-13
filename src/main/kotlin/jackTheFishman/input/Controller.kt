package jackTheFishman.input

import jackTheFishman.math.constants.vector2f.zero
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWGamepadState


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

    var leftStick = zero

    var rightStick = zero

    // der button state, der allen buttons beim konstruieren gegeben wird
    private val initialButtonState = ToggleableState(isDown = false, changed = false)

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

                buttonStates[it] = ToggleableState(
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
                buttonStates[it] = ToggleableState(
                    isDown = false,
                    changed = buttonStates[it]!!.isDown
                )
            }

            leftStick = zero
            rightStick = zero
        }
    }

    val isPresent: Boolean
        get() = glfwJoystickPresent(GLFW_JOYSTICK_1) && glfwJoystickIsGamepad(GLFW_JOYSTICK_1)
}
