package jackTheFishman.input

import jackTheFishman.Window
import jackTheFishman.math.constants.vector2f.zero
import jackTheFishman.math.extentions.minus
import org.joml.Vector2fc

class WindowMouse(private val window: Window) : Mouse {
    override var position: Vector2fc = zero
    override var deltaPosition: Vector2fc = zero

    // TODO replace with cached buttons
    override var leftButton = ToggleableState(isDown = false, changed = false)
    override var rightButton = ToggleableState(isDown = false, changed = false)

    override var cursorMode: CursorMode = CursorMode.NORMAL
        set(value) {
            window.cursorMode = value
            field = value
        }
        get() = window.cursorMode

    init {
        window.onUpdate.subscribe {
            update()
        }
    }

    private fun update() {
        val previousPosition = position
        position = window.mousePosition
        deltaPosition = position - previousPosition

        val isLeftDown = window.isLeftMouseButtonDown
        val isRightDown = window.isRightMouseButtonDown

        leftButton = ToggleableState(isLeftDown, isLeftDown != leftButton.isDown)
        rightButton = ToggleableState(isRightDown, isRightDown != rightButton.isDown)
    }
}
