package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector2fc

open class LeguiComponentWrapper<out T>(val leguiComponent: T, var onPressed: () -> Unit = {}) : Component() where T : org.liquidengine.legui.component.Component {
    var position: Vector2fc
        get() = Vector2f(leguiComponent.position)
        set(value) {
            leguiComponent.position = Vector2f(value)
        }

    var scaledPosition: Vector2fc
        get() = Vector2f(position).div(Window.contentScale)
        set(value) {
            position = Vector2f(value).mul(Window.contentScale)
        }

    var size: Vector2fc
        get() = Vector2f(leguiComponent.size)
        set(value) {
            leguiComponent.size = Vector2f(value)
        }

    var scaledSize: Vector2fc
        get() = Vector2f(size).div(Window.contentScale)
        set(value) {
            size = Vector2f(value).mul(Window.contentScale)
        }

    private var wasPressedLastUpdate = false

    override fun start() {
        super.start()
        Scene.active.gui.add(leguiComponent)
    }

    override fun stop() {
        Scene.active.gui.remove(leguiComponent)
        super.stop()
    }

    override fun update() {
        if (leguiComponent.isPressed && !wasPressedLastUpdate) {
            onPressed()
            wasPressedLastUpdate = true
        } else if (!leguiComponent.isPressed && wasPressedLastUpdate) {
            wasPressedLastUpdate = false
        }
    }

    override fun draw() {
    }
}
