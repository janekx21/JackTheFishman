package jackTheFishman.input

/**
 * Immutable button state
 */
data class ToggleableState(val isDown: Boolean, val changed: Boolean) {
    val justDown = isDown && changed
    val justUp = !isDown && changed
}
