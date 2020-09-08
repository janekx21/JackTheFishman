package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.audio.IPlayable
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.Source

class Audio : Component(), IPlayable {
    var source = Source()

    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    override fun play() = source.play()

    override fun pause() = source.pause()

    override fun stop() {
        source.stop()
        source.close()
        //source.sample?.close()
    }

    override var time: Float
        set(value) {
            source.time = value
        }
        get() = source.time

    override val playing: Boolean get() = source.playing

    override fun update() {
        source.pitch = Time.timeScale
        source.position = transform.position
    }

    override fun draw() {}
}
