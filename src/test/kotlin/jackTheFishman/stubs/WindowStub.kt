package jackTheFishman.stubs

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.ReplaySubject
import jackTheFishman.Window
import jackTheFishman.graphics.Texture2D
import jackTheFishman.input.CursorMode
import jackTheFishman.input.KeyboardAction
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector2i
import org.joml.Vector2ic

class WindowStub : Window {
    override var shouldClose: Boolean = false
    override var physicalSize: Vector2ic = Vector2i(1280, 720)
    override val logicalSize: Vector2fc = Vector2f(1280f, 720f)
    override var contentScale: Float= 1f
    override var fullscreen: Boolean = false
    override val aspect: Float
        get() = logicalSize.x() / logicalSize.y()

    var updateSubject: ReplaySubject<Float> = ReplaySubject.create()
    override val onBetweenUpdates: Observable<Float> = updateSubject

    var keyboardSubject: ReplaySubject<KeyboardAction> = ReplaySubject.create()
    override val onKeyChanged: Observable<KeyboardAction> = keyboardSubject

    override fun setCursor(texture: Texture2D) {}

    override fun setIcon(texture: Texture2D) {}

    override fun update() {}

    override fun setCursorMode(mode: CursorMode) {}

    override var mousePosition: Vector2fc = Vector2f(0f, 0f)
    override val isLeftMouseButtonDown: Boolean = false
    override val isRightMouseButtonDown: Boolean = false
}