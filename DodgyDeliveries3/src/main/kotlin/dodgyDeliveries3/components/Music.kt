package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.audio.IPlayable
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.Source

class Music : Component(), IPlayable {
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
    override var time: Float
        set(value) {
            source.time = value
        }
        get() = source.time
    override val playing: Boolean get() = source.playing

    override fun update() {}

    override fun draw() {}

    val beat: Float
        get() = (time - offset) / (60f / bpm)
}
