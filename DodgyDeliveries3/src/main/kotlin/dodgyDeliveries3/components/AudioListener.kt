package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import jackTheFishman.engine.Audio
import jackTheFishman.engine.math.Vector3fCopy
import org.joml.Quaternionf

class AudioListener(gameObject: GameObject) : Component(gameObject) {
    override fun update() {
        Audio.Listener.position = transform.generateMatrix().getTranslation(Vector3fCopy.zero)
        Audio.Listener.rotation = transform.generateMatrix().getNormalizedRotation(Quaternionf())
    }

    override fun draw() {}

    override fun toJson(): Any? {
        return mapOf<String, Any?>()
    }

    override fun fromJson(json: Any?) {
        check(json is Map<*, *>)
    }
}