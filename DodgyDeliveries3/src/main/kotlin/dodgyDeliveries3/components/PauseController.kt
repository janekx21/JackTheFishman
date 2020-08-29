package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Input
import jackTheFishman.engine.Time

class PauseController : Component() {
    override fun update() {
        if (Input.Keyboard.justDown(Input.Keyboard.Keys.KEY_K)) {
            Time.timeScale = 0f
        }
    }
}
