package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Audio

class AudioListener : Component() {
    override fun update() {
        Audio.Listener.position = transform.generateMatrix().getTranslation(Vector3fCopy.zero)
        Audio.Listener.rotation = transform.generateMatrix().getNormalizedRotation(Quaternionf())
    }

    override fun draw() {}
}