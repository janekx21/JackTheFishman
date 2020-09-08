package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.audio.IPlayable
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.Source
import org.lwjgl.openal.AL10.AL_INVALID_NAME
import org.lwjgl.openal.AL10.alGetError

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

    override fun stop() = source.stop()

    override var time: Float
        set(value) {
            source.time = value
        }
        get() = source.time

    override val playing: Boolean get() = source.playing

    override fun update() {
        source.pitch = Time.timeScale
        source.position = transform.position
        val error = alGetError()
        if (error != 0) {
            println(error)
            println(AL_INVALID_NAME)
        }
    }

    override fun draw() {}
}
