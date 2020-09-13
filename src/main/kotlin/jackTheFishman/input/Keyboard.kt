package jackTheFishman.input

import jackTheFishman.Window
import org.lwjgl.glfw.GLFW

/**
 * Interface for keyboard interactions like pressing and releasing keys.
 * You can get a specific key state using these functions respectively:
 * [down], [up], [changed], [justDown] and [justUp].
 */
class WindowKeyboard(val window: Window) {
    private val glfwKeyToKey = mapOf(
        GLFW.GLFW_KEY_SPACE to Keys.SPACE,
        GLFW.GLFW_KEY_APOSTROPHE to Keys.APOSTROPHE,
        GLFW.GLFW_KEY_COMMA to Keys.COMMA,
        GLFW.GLFW_KEY_MINUS to Keys.MINUS,
        GLFW.GLFW_KEY_PERIOD to Keys.PERIOD,
        GLFW.GLFW_KEY_SLASH to Keys.SLASH,
        GLFW.GLFW_KEY_0 to Keys.NUMBER_0,
        GLFW.GLFW_KEY_1 to Keys.NUMBER_1,
        GLFW.GLFW_KEY_2 to Keys.NUMBER_2,
        GLFW.GLFW_KEY_3 to Keys.NUMBER_3,
        GLFW.GLFW_KEY_4 to Keys.NUMBER_4,
        GLFW.GLFW_KEY_5 to Keys.NUMBER_5,
        GLFW.GLFW_KEY_6 to Keys.NUMBER_6,
        GLFW.GLFW_KEY_7 to Keys.NUMBER_7,
        GLFW.GLFW_KEY_8 to Keys.NUMBER_8,
        GLFW.GLFW_KEY_9 to Keys.NUMBER_9,
        GLFW.GLFW_KEY_SEMICOLON to Keys.SEMICOLON,
        GLFW.GLFW_KEY_EQUAL to Keys.EQUAL,
        GLFW.GLFW_KEY_A to Keys.A,
        GLFW.GLFW_KEY_B to Keys.B,
        GLFW.GLFW_KEY_C to Keys.C,
        GLFW.GLFW_KEY_D to Keys.D,
        GLFW.GLFW_KEY_E to Keys.E,
        GLFW.GLFW_KEY_F to Keys.F,
        GLFW.GLFW_KEY_G to Keys.G,
        GLFW.GLFW_KEY_H to Keys.H,
        GLFW.GLFW_KEY_I to Keys.I,
        GLFW.GLFW_KEY_J to Keys.J,
        GLFW.GLFW_KEY_K to Keys.K,
        GLFW.GLFW_KEY_L to Keys.L,
        GLFW.GLFW_KEY_M to Keys.M,
        GLFW.GLFW_KEY_N to Keys.N,
        GLFW.GLFW_KEY_O to Keys.O,
        GLFW.GLFW_KEY_P to Keys.P,
        GLFW.GLFW_KEY_Q to Keys.Q,
        GLFW.GLFW_KEY_R to Keys.R,
        GLFW.GLFW_KEY_S to Keys.S,
        GLFW.GLFW_KEY_T to Keys.T,
        GLFW.GLFW_KEY_U to Keys.U,
        GLFW.GLFW_KEY_V to Keys.V,
        GLFW.GLFW_KEY_W to Keys.W,
        GLFW.GLFW_KEY_X to Keys.X,
        GLFW.GLFW_KEY_Y to Keys.Y,
        GLFW.GLFW_KEY_Z to Keys.Z,
        GLFW.GLFW_KEY_LEFT_BRACKET to Keys.LEFT_BRACKET,
        GLFW.GLFW_KEY_BACKSLASH to Keys.BACKSLASH,
        GLFW.GLFW_KEY_RIGHT_BRACKET to Keys.RIGHT_BRACKET,
        GLFW.GLFW_KEY_GRAVE_ACCENT to Keys.GRAVE_ACCENT,
        GLFW.GLFW_KEY_WORLD_1 to Keys.WORLD_1,
        GLFW.GLFW_KEY_WORLD_2 to Keys.WORLD_2,
        GLFW.GLFW_KEY_ESCAPE to Keys.ESCAPE,
        GLFW.GLFW_KEY_ENTER to Keys.ENTER,
        GLFW.GLFW_KEY_TAB to Keys.TAB,
        GLFW.GLFW_KEY_BACKSPACE to Keys.BACKSPACE,
        GLFW.GLFW_KEY_INSERT to Keys.INSERT,
        GLFW.GLFW_KEY_DELETE to Keys.DELETE,
        GLFW.GLFW_KEY_RIGHT to Keys.RIGHT,
        GLFW.GLFW_KEY_LEFT to Keys.LEFT,
        GLFW.GLFW_KEY_DOWN to Keys.DOWN,
        GLFW.GLFW_KEY_UP to Keys.UP,
        GLFW.GLFW_KEY_PAGE_UP to Keys.PAGE_UP,
        GLFW.GLFW_KEY_PAGE_DOWN to Keys.PAGE_DOWN,
        GLFW.GLFW_KEY_HOME to Keys.HOME,
        GLFW.GLFW_KEY_END to Keys.END,
        GLFW.GLFW_KEY_CAPS_LOCK to Keys.CAPS_LOCK,
        GLFW.GLFW_KEY_SCROLL_LOCK to Keys.SCROLL_LOCK,
        GLFW.GLFW_KEY_NUM_LOCK to Keys.NUM_LOCK,
        GLFW.GLFW_KEY_PRINT_SCREEN to Keys.PRINT_SCREEN,
        GLFW.GLFW_KEY_PAUSE to Keys.PAUSE,
        GLFW.GLFW_KEY_F1 to Keys.F1,
        GLFW.GLFW_KEY_F2 to Keys.F2,
        GLFW.GLFW_KEY_F3 to Keys.F3,
        GLFW.GLFW_KEY_F4 to Keys.F4,
        GLFW.GLFW_KEY_F5 to Keys.F5,
        GLFW.GLFW_KEY_F6 to Keys.F6,
        GLFW.GLFW_KEY_F7 to Keys.F7,
        GLFW.GLFW_KEY_F8 to Keys.F8,
        GLFW.GLFW_KEY_F9 to Keys.F9,
        GLFW.GLFW_KEY_F10 to Keys.F10,
        GLFW.GLFW_KEY_F11 to Keys.F11,
        GLFW.GLFW_KEY_F12 to Keys.F12,
        GLFW.GLFW_KEY_F13 to Keys.F13,
        GLFW.GLFW_KEY_F14 to Keys.F14,
        GLFW.GLFW_KEY_F15 to Keys.F15,
        GLFW.GLFW_KEY_F16 to Keys.F16,
        GLFW.GLFW_KEY_F17 to Keys.F17,
        GLFW.GLFW_KEY_F18 to Keys.F18,
        GLFW.GLFW_KEY_F19 to Keys.F19,
        GLFW.GLFW_KEY_F20 to Keys.F20,
        GLFW.GLFW_KEY_F21 to Keys.F21,
        GLFW.GLFW_KEY_F22 to Keys.F22,
        GLFW.GLFW_KEY_F23 to Keys.F23,
        GLFW.GLFW_KEY_F24 to Keys.F24,
        GLFW.GLFW_KEY_F25 to Keys.F25,
        GLFW.GLFW_KEY_KP_0 to Keys.KEYPAD_0,
        GLFW.GLFW_KEY_KP_1 to Keys.KEYPAD_1,
        GLFW.GLFW_KEY_KP_2 to Keys.KEYPAD_2,
        GLFW.GLFW_KEY_KP_3 to Keys.KEYPAD_3,
        GLFW.GLFW_KEY_KP_4 to Keys.KEYPAD_4,
        GLFW.GLFW_KEY_KP_5 to Keys.KEYPAD_5,
        GLFW.GLFW_KEY_KP_6 to Keys.KEYPAD_6,
        GLFW.GLFW_KEY_KP_7 to Keys.KEYPAD_7,
        GLFW.GLFW_KEY_KP_8 to Keys.KEYPAD_8,
        GLFW.GLFW_KEY_KP_9 to Keys.KEYPAD_9,
        GLFW.GLFW_KEY_KP_DECIMAL to Keys.KEYPAD_DECIMAL,
        GLFW.GLFW_KEY_KP_DIVIDE to Keys.KEYPAD_DIVIDE,
        GLFW.GLFW_KEY_KP_MULTIPLY to Keys.KEYPAD_MULTIPLY,
        GLFW.GLFW_KEY_KP_SUBTRACT to Keys.KEYPAD_SUBTRACT,
        GLFW.GLFW_KEY_KP_ADD to Keys.KEYPAD_ADD,
        GLFW.GLFW_KEY_KP_ENTER to Keys.KEYPAD_ENTER,
        GLFW.GLFW_KEY_KP_EQUAL to Keys.KEYPAD_EQUAL,
        GLFW.GLFW_KEY_LEFT_SHIFT to Keys.LEFT_SHIFT,
        GLFW.GLFW_KEY_LEFT_CONTROL to Keys.LEFT_CONTROL,
        GLFW.GLFW_KEY_LEFT_ALT to Keys.LEFT_ALT,
        GLFW.GLFW_KEY_LEFT_SUPER to Keys.LEFT_SUPER,
        GLFW.GLFW_KEY_RIGHT_SHIFT to Keys.RIGHT_SHIFT,
        GLFW.GLFW_KEY_RIGHT_CONTROL to Keys.RIGHT_CONTROL,
        GLFW.GLFW_KEY_RIGHT_ALT to Keys.RIGHT_ALT,
        GLFW.GLFW_KEY_RIGHT_SUPER to Keys.RIGHT_SUPER,
        GLFW.GLFW_KEY_MENU to Keys.MENU
    )

    private var keyStates =
        Keys.values().map { key -> Pair(key, ToggleableState(isDown = false, changed = false)) }.toMap()

    /**
     * Collection of all key states that should be applied in the next update
     */
    private var nextKeyStates = keyStates.toMutableMap()

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
            GLFW.GLFW_PRESS -> {
                nextKeyStates[key] = ToggleableState(
                        isDown = true,
                        changed = !isDown
                )
            }
            GLFW.GLFW_RELEASE -> {
                nextKeyStates[key] = ToggleableState(
                        isDown = false,
                        changed = isDown
                )
            }
            else -> {
                check(glfwAction == GLFW.GLFW_REPEAT)
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
