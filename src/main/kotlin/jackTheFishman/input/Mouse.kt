package jackTheFishman.input

import org.joml.Vector2fc

/**
 * Interface for mouse interactions
 */
interface Mouse {
    var position: Vector2fc
    var deltaPosition: Vector2fc

    var leftButton: ToggleableState
    var rightButton: ToggleableState
    var cursorMode: CursorMode
}
