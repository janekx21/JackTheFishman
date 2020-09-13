package jackTheFishman.input

/**
 * Immutable button state
 */
data class ToggleableState(val isDown: Boolean = false, val changed: Boolean = false) {
    val justDown = isDown && changed
    val justUp = !isDown && changed
}
