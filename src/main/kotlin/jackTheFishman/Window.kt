package jackTheFishman

import io.reactivex.rxjava3.core.Observable
import jackTheFishman.graphics.Texture2D
import jackTheFishman.input.CursorMode
import jackTheFishman.input.KeyboardAction
import org.joml.Vector2fc
import org.joml.Vector2ic

interface Window {
    val physicalSize: Vector2ic
    val logicalSize: Vector2fc
    val aspect: Float

    val onUpdate: Observable<Float>
    val onKeyAction: Observable<KeyboardAction>

    var shouldClose: Boolean
    var contentScale: Float
    var fullscreen: Boolean

    var cursor: Texture2D?
    var icon: Texture2D?
    var cursorMode: CursorMode

    val mousePosition: Vector2fc
    val isLeftMouseButtonDown: Boolean
    val isRightMouseButtonDown: Boolean

    fun update()
}
