package jackTheFishman.audio.openAl

import jackTheFishman.audio.PlayState
import jackTheFishman.audio.Source
import jackTheFishman.math.constants.vector3f.zero
import org.joml.Vector3fc
import java.io.Closeable

class OpenAlSource(initialSample: OpenAlSample? = null) : Source, Closeable {
    private val pointer = OpenAl.genSource()

    override var sample: OpenAlSample? = null
        set(value) {
            if (value != null) {
                OpenAl.setSampleOfSource(pointer, value.pointer)
            }
            field = value
        }

    init {
        sample = initialSample
    }

    override var pitch: Float = 1.0f
        set(value) {
            OpenAl.setSourcePitch(pointer, value)
            field = value
        }

    override var gain: Float = 1.0f
        set(value) {
            OpenAl.setSourceGain(pointer, gain)
            field = value
        }

    override var position: Vector3fc = zero
        set(value) {
            OpenAl.setSourcePosition(pointer, value)
            field = value
        }

    override var velocity: Vector3fc = zero
        set(value) {
            OpenAl.setSourceVelocity(pointer, value)
            field = value
        }

    override var looping: Boolean = false
        set(value) {
            OpenAl.setSourceLooping(pointer, value)
            field = value
        }

    override fun play() {
        OpenAl.sourcePlay(pointer)
    }

    override fun pause() {
        OpenAl.sourcePause(pointer)
    }

    override fun stop() {
        OpenAl.sourceStop(pointer)
    }

    override var timeInSeconds: Float
        set(value) = OpenAl.setSourceOffset(pointer, value)
        get() = OpenAl.getSourceOffset(pointer)

    override val state: PlayState
        get() = OpenAl.getSourceState(pointer)

    override val isPlaying: Boolean
        get() = state == PlayState.PLAYING

    override fun close() {
        stop()
        delete()
    }

    private fun delete() {
        OpenAl.deleteSource(pointer)
    }
}
