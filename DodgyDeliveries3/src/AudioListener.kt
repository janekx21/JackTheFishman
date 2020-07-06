import engine.Audio
import engine.math.Vector3fCopy
import org.joml.Quaternionf

class AudioListener(gameObject: GameObject) : Component(gameObject) {
    override fun update() {
        Audio.Listener.position = transform.generateMatrix().getTranslation(Vector3fCopy.zero)
        Audio.Listener.rotation = transform.generateMatrix().getNormalizedRotation(Quaternionf())
    }

    override fun draw() {}
}