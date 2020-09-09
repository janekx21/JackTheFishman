package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.audio.IPlayable
import jackTheFishman.engine.audio.PlayState
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.Source

class Music(loop: Boolean = false, var onEndReached: () -> Unit = {}) : Component(), IPlayable {
    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    var bpm = 100f
    var offset = 0f

    val beat: Float
        get() = (time - offset) / (60f / bpm)

    val secondsPerBeat: Float
        get() = 60f / bpm

    val source = Source().also { it.looping = loop }

    override fun play() = source.play()
    override fun pause() = source.pause()
    override fun stop() {
        source.stop()
        source.close()
        source.sample?.close()
    }

    override var time: Float
        set(value) {
            source.time = value
        }
        get() = source.time

    override val playing: Boolean get() = source.playing

    private var lastPlayState: PlayState? = null

    override fun update() {
        super.update()

        source.pitch = Time.timeScale

        if (source.state != lastPlayState) {
            if (source.state == PlayState.STOPPED) {
                onEndReached()
            }
            lastPlayState = source.state
        }
    }
}
