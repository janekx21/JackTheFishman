package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Input

class EscapeHandler(var action: () -> Unit = {}) : Component() {
    override fun update() {
        if (Input.Keyboard.justDown(Input.Keyboard.Keys.KEY_ESCAPE)) {
            action()
        }
    }
}
