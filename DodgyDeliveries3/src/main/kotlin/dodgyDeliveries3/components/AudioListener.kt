package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Audio

class AudioListener : Component() {
    override fun update() {
        Audio.Listener.position = transform.position
        Audio.Listener.rotation = transform.rotation
    }

    override fun draw() {}
}
