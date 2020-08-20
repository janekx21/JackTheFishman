package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene

open class LeguiComponent(private val leguiComponent: org.liquidengine.legui.component.Component) : Component() {
    override fun start() {
        super.start()
        Scene.active.gui.add(leguiComponent)
    }

    override fun stop() {
        super.stop()
        Scene.active.gui.add(leguiComponent)
    }

    override fun update() {
    }

    override fun draw() {
    }
}
