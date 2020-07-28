package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Audio
import jackTheFishman.engine.math.Vector3fCopy
import org.joml.Quaternionf

class AudioListener : Component() {
    override fun update() {
        Audio.Listener.position = transform.generateMatrix().getTranslation(Vector3fCopy.zero)
        Audio.Listener.rotation = transform.generateMatrix().getNormalizedRotation(Quaternionf())
    }

    override fun draw() {}
}