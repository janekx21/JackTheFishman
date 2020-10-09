package jackTheFishman.input

import jackTheFishman.Window

/**
 * Interface for keyboard interactions like pressing and releasing keys.
 * You can get a specific key state using these functions respectively:
 * [down], [up], [changed], [justDown] and [justUp].
 */
class WindowKeyboard(private val window: Window) : Keyboard {
    private var keyStates =
        KeyboardKey.values().map { key -> key to ToggleableState() }.toMap()

    /**
     * Collection of all key states that should be applied in the next update
     */
    private var nextKeyStates = keyStates.toMutableMap()

    init {
        window.onKeyAction.subscribe { registerKeyboardAction(it) }
        window.onUpdate.subscribe { update() }
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

    override fun down(key: KeyboardKey): Boolean {
        return keyStates.getOrDefault(key, ToggleableState()).isDown
    }

    override fun up(key: KeyboardKey): Boolean {
        return !down(key)
    }

    override fun changed(key: KeyboardKey): Boolean {
        return keyStates.getOrDefault(key, ToggleableState()).changed
    }

    override fun justDown(key: KeyboardKey): Boolean {
        return keyStates.getOrDefault(key, ToggleableState()).justDown
    }

    override fun justUp(key: KeyboardKey): Boolean {
        return keyStates.getOrDefault(key, ToggleableState()).justUp
    }
}
