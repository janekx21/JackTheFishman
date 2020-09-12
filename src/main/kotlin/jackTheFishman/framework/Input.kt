package jackTheFishman.framework

import jackTheFishman.framework.Input.Keyboard.changed
import jackTheFishman.framework.Input.Keyboard.down
import jackTheFishman.framework.Input.Keyboard.justDown
import jackTheFishman.framework.Input.Keyboard.justUp
import jackTheFishman.framework.Input.Keyboard.up
import jackTheFishman.framework.math.constants.vector2f.zero
import jackTheFishman.framework.math.extentions.minus
import jackTheFishman.framework.util.pointer.DoublePointer
import org.joml.Vector2f
import org.joml.Vector2fc
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWGamepadState

/**
 * Interface for all user interactions delegated to sub objects
 */
object Input {
    /**
     * Immutable button state
     */
    data class ToggleableState(val isDown: Boolean, val changed: Boolean) {
        val justDown = isDown && changed
        val justUp = !isDown && changed
    }

    fun update() {
        Keyboard.update()
        Mouse.update()
        Controller.update()
    }

    /**
     * Interface for keyboard interactions like pressing and releasing keys.
     * You can get a specific key state using these functions respectively:
     * [down], [up], [changed], [justDown] and [justUp].
     */
    object Keyboard {
        enum class Keys {
            KEY_SPACE,
            KEY_APOSTROPHE,
            KEY_COMMA,
            KEY_MINUS,
            KEY_PERIOD,
            KEY_SLASH,
            KEY_0,
            KEY_1,
            KEY_2,
            KEY_3,
            KEY_4,
            KEY_5,
            KEY_6,
            KEY_7,
            KEY_8,
            KEY_9,
            KEY_SEMICOLON,
            KEY_EQUAL,
            KEY_A,
            KEY_B,
            KEY_C,
            KEY_D,
            KEY_E,
            KEY_F,
            KEY_G,
            KEY_H,
            KEY_I,
            KEY_J,
            KEY_K,
            KEY_L,
            KEY_M,
            KEY_N,
            KEY_O,
            KEY_P,
            KEY_Q,
            KEY_R,
            KEY_S,
            KEY_T,
            KEY_U,
            KEY_V,
            KEY_W,
            KEY_X,
            KEY_Y,
            KEY_Z,
            KEY_LEFT_BRACKET,
            KEY_BACKSLASH,
            KEY_RIGHT_BRACKET,
            KEY_GRAVE_ACCENT,
            KEY_WORLD_1,
            KEY_WORLD_2,
            KEY_ESCAPE,
            KEY_ENTER,
            KEY_TAB,
            KEY_BACKSPACE,
            KEY_INSERT,
            KEY_DELETE,
            KEY_RIGHT,
            KEY_LEFT,
            KEY_DOWN,
            KEY_UP,
            KEY_PAGE_UP,
            KEY_PAGE_DOWN,
            KEY_HOME,
            KEY_END,
            KEY_CAPS_LOCK,
            KEY_SCROLL_LOCK,
            KEY_NUM_LOCK,
            KEY_PRINT_SCREEN,
            KEY_PAUSE,
            KEY_F1,
            KEY_F2,
            KEY_F3,
            KEY_F4,
            KEY_F5,
            KEY_F6,
            KEY_F7,
            KEY_F8,
            KEY_F9,
            KEY_F10,
            KEY_F11,
            KEY_F12,
            KEY_F13,
            KEY_F14,
            KEY_F15,
            KEY_F16,
            KEY_F17,
            KEY_F18,
            KEY_F19,
            KEY_F20,
            KEY_F21,
            KEY_F22,
            KEY_F23,
            KEY_F24,
            KEY_F25,
            KEY_KP_0,
            KEY_KP_1,
            KEY_KP_2,
            KEY_KP_3,
            KEY_KP_4,
            KEY_KP_5,
            KEY_KP_6,
            KEY_KP_7,
            KEY_KP_8,
            KEY_KP_9,
            KEY_KP_DECIMAL,
            KEY_KP_DIVIDE,
            KEY_KP_MULTIPLY,
            KEY_KP_SUBTRACT,
            KEY_KP_ADD,
            KEY_KP_ENTER,
            KEY_KP_EQUAL,
            KEY_LEFT_SHIFT,
            KEY_LEFT_CONTROL,
            KEY_LEFT_ALT,
            KEY_LEFT_SUPER,
            KEY_RIGHT_SHIFT,
            KEY_RIGHT_CONTROL,
            KEY_RIGHT_ALT,
            KEY_RIGHT_SUPER,
            KEY_MENU
        }

        private val glfwKeyToKey = mapOf(
            GLFW_KEY_SPACE to Keys.KEY_SPACE,
            GLFW_KEY_APOSTROPHE to Keys.KEY_APOSTROPHE,
            GLFW_KEY_COMMA to Keys.KEY_COMMA,
            GLFW_KEY_MINUS to Keys.KEY_MINUS,
            GLFW_KEY_PERIOD to Keys.KEY_PERIOD,
            GLFW_KEY_SLASH to Keys.KEY_SLASH,
            GLFW_KEY_0 to Keys.KEY_0,
            GLFW_KEY_1 to Keys.KEY_1,
            GLFW_KEY_2 to Keys.KEY_2,
            GLFW_KEY_3 to Keys.KEY_3,
            GLFW_KEY_4 to Keys.KEY_4,
            GLFW_KEY_5 to Keys.KEY_5,
            GLFW_KEY_6 to Keys.KEY_6,
            GLFW_KEY_7 to Keys.KEY_7,
            GLFW_KEY_8 to Keys.KEY_8,
            GLFW_KEY_9 to Keys.KEY_9,
            GLFW_KEY_SEMICOLON to Keys.KEY_SEMICOLON,
            GLFW_KEY_EQUAL to Keys.KEY_EQUAL,
            GLFW_KEY_A to Keys.KEY_A,
            GLFW_KEY_B to Keys.KEY_B,
            GLFW_KEY_C to Keys.KEY_C,
            GLFW_KEY_D to Keys.KEY_D,
            GLFW_KEY_E to Keys.KEY_E,
            GLFW_KEY_F to Keys.KEY_F,
            GLFW_KEY_G to Keys.KEY_G,
            GLFW_KEY_H to Keys.KEY_H,
            GLFW_KEY_I to Keys.KEY_I,
            GLFW_KEY_J to Keys.KEY_J,
            GLFW_KEY_K to Keys.KEY_K,
            GLFW_KEY_L to Keys.KEY_L,
            GLFW_KEY_M to Keys.KEY_M,
            GLFW_KEY_N to Keys.KEY_N,
            GLFW_KEY_O to Keys.KEY_O,
            GLFW_KEY_P to Keys.KEY_P,
            GLFW_KEY_Q to Keys.KEY_Q,
            GLFW_KEY_R to Keys.KEY_R,
            GLFW_KEY_S to Keys.KEY_S,
            GLFW_KEY_T to Keys.KEY_T,
            GLFW_KEY_U to Keys.KEY_U,
            GLFW_KEY_V to Keys.KEY_V,
            GLFW_KEY_W to Keys.KEY_W,
            GLFW_KEY_X to Keys.KEY_X,
            GLFW_KEY_Y to Keys.KEY_Y,
            GLFW_KEY_Z to Keys.KEY_Z,
            GLFW_KEY_LEFT_BRACKET to Keys.KEY_LEFT_BRACKET,
            GLFW_KEY_BACKSLASH to Keys.KEY_BACKSLASH,
            GLFW_KEY_RIGHT_BRACKET to Keys.KEY_RIGHT_BRACKET,
            GLFW_KEY_GRAVE_ACCENT to Keys.KEY_GRAVE_ACCENT,
            GLFW_KEY_WORLD_1 to Keys.KEY_WORLD_1,
            GLFW_KEY_WORLD_2 to Keys.KEY_WORLD_2,
            GLFW_KEY_ESCAPE to Keys.KEY_ESCAPE,
            GLFW_KEY_ENTER to Keys.KEY_ENTER,
            GLFW_KEY_TAB to Keys.KEY_TAB,
            GLFW_KEY_BACKSPACE to Keys.KEY_BACKSPACE,
            GLFW_KEY_INSERT to Keys.KEY_INSERT,
            GLFW_KEY_DELETE to Keys.KEY_DELETE,
            GLFW_KEY_RIGHT to Keys.KEY_RIGHT,
            GLFW_KEY_LEFT to Keys.KEY_LEFT,
            GLFW_KEY_DOWN to Keys.KEY_DOWN,
            GLFW_KEY_UP to Keys.KEY_UP,
            GLFW_KEY_PAGE_UP to Keys.KEY_PAGE_UP,
            GLFW_KEY_PAGE_DOWN to Keys.KEY_PAGE_DOWN,
            GLFW_KEY_HOME to Keys.KEY_HOME,
            GLFW_KEY_END to Keys.KEY_END,
            GLFW_KEY_CAPS_LOCK to Keys.KEY_CAPS_LOCK,
            GLFW_KEY_SCROLL_LOCK to Keys.KEY_SCROLL_LOCK,
            GLFW_KEY_NUM_LOCK to Keys.KEY_NUM_LOCK,
            GLFW_KEY_PRINT_SCREEN to Keys.KEY_PRINT_SCREEN,
            GLFW_KEY_PAUSE to Keys.KEY_PAUSE,
            GLFW_KEY_F1 to Keys.KEY_F1,
            GLFW_KEY_F2 to Keys.KEY_F2,
            GLFW_KEY_F3 to Keys.KEY_F3,
            GLFW_KEY_F4 to Keys.KEY_F4,
            GLFW_KEY_F5 to Keys.KEY_F5,
            GLFW_KEY_F6 to Keys.KEY_F6,
            GLFW_KEY_F7 to Keys.KEY_F7,
            GLFW_KEY_F8 to Keys.KEY_F8,
            GLFW_KEY_F9 to Keys.KEY_F9,
            GLFW_KEY_F10 to Keys.KEY_F10,
            GLFW_KEY_F11 to Keys.KEY_F11,
            GLFW_KEY_F12 to Keys.KEY_F12,
            GLFW_KEY_F13 to Keys.KEY_F13,
            GLFW_KEY_F14 to Keys.KEY_F14,
            GLFW_KEY_F15 to Keys.KEY_F15,
            GLFW_KEY_F16 to Keys.KEY_F16,
            GLFW_KEY_F17 to Keys.KEY_F17,
            GLFW_KEY_F18 to Keys.KEY_F18,
            GLFW_KEY_F19 to Keys.KEY_F19,
            GLFW_KEY_F20 to Keys.KEY_F20,
            GLFW_KEY_F21 to Keys.KEY_F21,
            GLFW_KEY_F22 to Keys.KEY_F22,
            GLFW_KEY_F23 to Keys.KEY_F23,
            GLFW_KEY_F24 to Keys.KEY_F24,
            GLFW_KEY_F25 to Keys.KEY_F25,
            GLFW_KEY_KP_0 to Keys.KEY_KP_0,
            GLFW_KEY_KP_1 to Keys.KEY_KP_1,
            GLFW_KEY_KP_2 to Keys.KEY_KP_2,
            GLFW_KEY_KP_3 to Keys.KEY_KP_3,
            GLFW_KEY_KP_4 to Keys.KEY_KP_4,
            GLFW_KEY_KP_5 to Keys.KEY_KP_5,
            GLFW_KEY_KP_6 to Keys.KEY_KP_6,
            GLFW_KEY_KP_7 to Keys.KEY_KP_7,
            GLFW_KEY_KP_8 to Keys.KEY_KP_8,
            GLFW_KEY_KP_9 to Keys.KEY_KP_9,
            GLFW_KEY_KP_DECIMAL to Keys.KEY_KP_DECIMAL,
            GLFW_KEY_KP_DIVIDE to Keys.KEY_KP_DIVIDE,
            GLFW_KEY_KP_MULTIPLY to Keys.KEY_KP_MULTIPLY,
            GLFW_KEY_KP_SUBTRACT to Keys.KEY_KP_SUBTRACT,
            GLFW_KEY_KP_ADD to Keys.KEY_KP_ADD,
            GLFW_KEY_KP_ENTER to Keys.KEY_KP_ENTER,
            GLFW_KEY_KP_EQUAL to Keys.KEY_KP_EQUAL,
            GLFW_KEY_LEFT_SHIFT to Keys.KEY_LEFT_SHIFT,
            GLFW_KEY_LEFT_CONTROL to Keys.KEY_LEFT_CONTROL,
            GLFW_KEY_LEFT_ALT to Keys.KEY_LEFT_ALT,
            GLFW_KEY_LEFT_SUPER to Keys.KEY_LEFT_SUPER,
            GLFW_KEY_RIGHT_SHIFT to Keys.KEY_RIGHT_SHIFT,
            GLFW_KEY_RIGHT_CONTROL to Keys.KEY_RIGHT_CONTROL,
            GLFW_KEY_RIGHT_ALT to Keys.KEY_RIGHT_ALT,
            GLFW_KEY_RIGHT_SUPER to Keys.KEY_RIGHT_SUPER,
            GLFW_KEY_MENU to Keys.KEY_MENU
        )

        private var keyStates =
            Keys.values().map { key -> Pair(key, ToggleableState(isDown = false, changed = false)) }.toMap()

        /**
         * Collection of all key states that should be applied in the next update
         */
        private var nextKeyStates = keyStates.toMutableMap()

        @Deprecated("it depends on glfw key codes")
        fun down(key: Int): Boolean = down(glfwKeyToKey[key] ?: error("Key not found"))
        fun down(key: Keys): Boolean = keyStates.getValue(key).isDown

        @Deprecated("it depends on glfw key codes")
        fun up(key: Int): Boolean = up(glfwKeyToKey[key] ?: error("Key not found"))
        fun up(key: Keys): Boolean = !down(key)

        @Deprecated("it depends on glfw key codes")
        fun changed(key: Int): Boolean = changed(glfwKeyToKey[key] ?: error("Key not found"))
        fun changed(key: Keys): Boolean = keyStates.getValue(key).changed

        @Deprecated("it depends on glfw key codes")
        fun justDown(key: Int): Boolean = justDown(glfwKeyToKey[key] ?: error("Key not found"))
        fun justDown(key: Keys): Boolean = keyStates.getValue(key).justDown

        @Deprecated("it depends on glfw key codes")
        fun justUp(key: Int): Boolean = justUp(glfwKeyToKey[key] ?: error("Key not found"))
        fun justUp(key: Keys): Boolean = keyStates.getValue(key).justUp

        /**
         * Updates a key state. This does not change the key state immediately.
         * Therefore it does not change the return values of [down], [up], [changed], [justDown] and [justUp].
         */
        fun onKeyChanged(
            glfwKey: Int,
            glfwAction: Int
        ) {
            val key = glfwKeyToKey[glfwKey]
                ?: // this key is not in the map and should be ignored
                return
            val isDown = keyStates[key]?.isDown
            check(isDown != null) { "Key states is missing a key" }
            when (glfwAction) {
                GLFW_PRESS -> {
                    nextKeyStates[key] = ToggleableState(
                        isDown = true,
                        changed = !isDown
                    )
                }
                GLFW_RELEASE -> {
                    nextKeyStates[key] = ToggleableState(
                        isDown = false,
                        changed = isDown
                    )
                }
                else -> {
                    check(glfwAction == GLFW_REPEAT)
                }
            }
        }

        /**
         * Flushes the new key states.
         * Only when the update function is called the changes are made effective.
         */
        fun update() {
            keyStates = nextKeyStates.toMap()

            nextKeyStates = keyStates.mapValues {
                if (it.value.changed) {
                    ToggleableState(isDown = it.value.isDown, changed = false)
                } else {
                    it.value
                }
            }.toMutableMap()
        }
    }

    /**
     * Interface for mouse interactions
     */
    object Mouse {
        enum class CursorMode {
            DISABLED, HIDDEN, NORMAL
        }

        var position: Vector2fc = zero
        var deltaPosition: Vector2fc = zero

        var left = ToggleableState(isDown = false, changed = false)
        var right = ToggleableState(isDown = false, changed = false)

        fun setMode(mode: CursorMode) {
            val index = when (mode) {
                CursorMode.NORMAL -> GLFW_CURSOR_NORMAL
                CursorMode.DISABLED -> GLFW_CURSOR_DISABLED
                CursorMode.HIDDEN -> GLFW_CURSOR_HIDDEN
            }
            glfwSetInputMode(Window.pointer, GLFW_CURSOR, index)
        }

        fun update() {
            deltaPosition = Vector2f(position)

            val x = DoublePointer()
            val y = DoublePointer()

            glfwGetCursorPos(Window.pointer, x.buffer, y.buffer)

            position = Vector2f(x.value.toFloat(), y.value.toFloat())
            deltaPosition = position - deltaPosition

            val leftDown = glfwGetMouseButton(Window.pointer, GLFW_MOUSE_BUTTON_LEFT) != GLFW_RELEASE
            val rightDown = glfwGetMouseButton(Window.pointer, GLFW_MOUSE_BUTTON_RIGHT) != GLFW_RELEASE

            left = ToggleableState(isDown = leftDown, changed = leftDown != left.isDown)
            right = ToggleableState(isDown = rightDown, changed = rightDown != right.isDown)
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
}
