package jackTheFishman.input

import jackTheFishman.Window

/**
 * Interface for keyboard interactions like pressing and releasing keys.
 * You can get a specific key state using these functions respectively:
 * [down], [up], [changed], [justDown] and [justUp].
 */
class WindowKeyboard(private val window: Window) {
    private var keyStates =
        KeyboardKey.values().map { key -> key to ToggleableState() }.toMap()

    /**
     * Collection of all key states that should be applied in the next update
     */
    private var nextKeyStates = keyStates.toMutableMap()

    init {
        window.onKeyChanged.subscribe { registerKeyboardAction(it) }
        window.onBetweenUpdates.subscribe { update() }
    }

    private fun registerKeyboardAction(action: KeyboardAction) {
        val isDown = keyStates.getOrDefault(action.key, ToggleableState()).isDown

        when (action.action) {
            KeyboardActionType.PRESSED -> nextKeyStates[action.key] = ToggleableState(
                isDown = true,
                changed = !isDown
            )
            KeyboardActionType.RELEASED -> nextKeyStates[action.key] = ToggleableState(
                isDown = false,
                changed = isDown
            )
        }
    }

    /**
     * Flushes the new key states.
     * Only when the update function is called the changes are made effective.
     */
    private fun update() {
        keyStates = nextKeyStates.toMap()
        removeChanged()
    }

    private fun removeChanged() {
        nextKeyStates = keyStates.mapValues {
            if (it.value.changed) {
                ToggleableState(isDown = it.value.isDown, changed = false)
            } else {
                it.value
            }
        }.toMutableMap()
    }
}
