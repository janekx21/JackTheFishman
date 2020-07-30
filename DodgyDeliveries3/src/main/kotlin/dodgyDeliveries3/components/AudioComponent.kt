package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.audio.IPlayable
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.Source

class AudioComponent : Component(), IPlayable {
    private var source = Source()

    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    override fun play() = source.play()

    override fun pause() = source.pause()

    override fun stop() = source.stop()

    override var time: Float
        set(value) {
            source.time = value
        }
        get() = source.time

    override val playing: Boolean get() = source.playing

    override fun update() {
        // TODO make `transform.position` also be effected by parent transformation
        source.position = transform.position
    }

    override fun draw() {}
}
