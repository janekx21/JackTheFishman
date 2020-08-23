package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector2fc

open class LeguiComponent(protected val leguiComponent: org.liquidengine.legui.component.Component) : Component() {
    var position: Vector2fc = Vector2f(0F, 0F)
        get() = field
        set(value) {
            leguiComponent.position = Vector2f(value)
            field = value
        }

    var scaledPosition: Vector2fc
        get() = Vector2f(position).div(Window.contentScale)
        set(value) {
            position = Vector2f(value).mul(Window.contentScale)
        }

    override fun start() {
        super.start()
        Scene.active.gui.add(leguiComponent)
    }

    override fun stop() {
        super.stop()
        Scene.active.gui.remove(leguiComponent)
    }

    override fun update() {
    }

    override fun draw() {
    }
}
