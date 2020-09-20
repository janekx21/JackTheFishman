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
    override var shouldClose: Boolean
        get() = false
        set(value) {}
    override var physicalSize: Vector2ic
        get() = Vector2i(1280, 720)
        set(value) {}
    override val logicalSize: Vector2fc
        get() = Vector2f(1280f, 720f)
    override var contentScale: Float
        get() = 1f
        set(value) {}
    override var fullscreen: Boolean
        get() = false
        set(value) {}
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

    override val mousePosition: Vector2fc
        get() = Vector2f(42f, 65f)
    override val isLeftMouseButtonDown: Boolean
        get() = false
    override val isRightMouseButtonDown: Boolean
        get() = false
}
