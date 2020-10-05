package jackTheFishman

import io.reactivex.rxjava3.core.Observable
import jackTheFishman.graphics.Texture2D
import jackTheFishman.input.CursorMode
import jackTheFishman.input.KeyboardAction
import org.joml.Vector2fc
import org.joml.Vector2ic

interface Window {
    var shouldClose: Boolean
    val physicalSize: Vector2ic
    val logicalSize: Vector2fc
    var contentScale: Float
    var fullscreen: Boolean
    val aspect: Float
    val onBetweenUpdates: Observable<Float>
    val onKeyAction: Observable<KeyboardAction>

    fun setCursor(texture: Texture2D)
    fun setIcon(texture: Texture2D)
    fun update()

    fun setCursorMode(mode: CursorMode)
    val mousePosition: Vector2fc
    val isLeftMouseButtonDown: Boolean
    val isRightMouseButtonDown: Boolean
}
