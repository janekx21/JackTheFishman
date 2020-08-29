package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Window
import org.joml.Vector2f
import org.joml.Vector2fc

class LeguiWindowUpdate(var logicalSize: () -> Unit = {}, var logicalPosition: () -> Unit = {}) :
    Component() { // TODO: PLEASE REWORK AND ONLY USE ONE CALLBACK

    private var lastWindowSize: Vector2fc = Vector2f(0f, 0f)

    override fun update() {
        if (lastWindowSize != Window.logicalSize) {
            logicalSize()
            logicalPosition()
            lastWindowSize = Window.logicalSize
        }
    }
}
