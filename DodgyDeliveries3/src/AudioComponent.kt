import engine.audio.IPlayable
import engine.audio.Sample
import engine.audio.Source
import engine.math.Vector3fConst
import engine.math.Vector3fCopy
import org.joml.Vector3f

class AudioComponent(gameObject: GameObject) : Component(gameObject), IPlayable {
    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    private var source = Source()

    override fun play() = source.play()
    override fun pause() = source.pause()
    override fun stop() = source.stop()
    override val time: Float get() = source.time
    override val playing: Boolean get() = source.playing

    override fun update() {
        /// An dieser Stelle wäre es falsch, `transform.position` zu benutzen, da das
        /// nur die Translation eines einzelnen Transform Components wäre.
        /// Wir wollen aber die Translation aller Transform Componenten zusammengefasst haben.
        source.position = transform.generateMatrix().getTranslation(Vector3fCopy.zero)
    }

    override fun draw() {}
}