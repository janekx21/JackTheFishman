import engine.audio.IPlayable
import engine.audio.Sample
import engine.audio.Source

class Music(gameObject: GameObject) : Component(gameObject), IPlayable {

    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    var bpm = 100f
    var offset = 0f

    private var source = Source()

    override fun play() = source.play()
    override fun pause() = source.pause()
    override fun stop() = source.stop()
    override val time: Float get() = source.time
    override val playing: Boolean get() = source.playing

    override fun update() {}

    override fun draw() {}

    val beat: Float
        get() = (time - offset) / (60f / bpm)
}