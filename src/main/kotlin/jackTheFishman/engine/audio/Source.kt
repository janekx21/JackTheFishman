package jackTheFishman.engine.audio

import jackTheFishman.engine.math.Vector3fConst
import org.joml.Vector3fc
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.AL11.AL_SEC_OFFSET
import java.io.Closeable

/**
 * Represents a audio sample like a sound
 */
class Source(initialSample: Sample? = null) : Closeable, IPlayable {
    private val pointer = alGenSources()
    var sample: Sample? = null
        set(value) {
            if (value != null) {
                alSourcei(pointer, AL_BUFFER, value.pointer)
            }
            field = value
        }

    init {
        sample = initialSample
    }

    var pitch: Float = 1.0f
        set(value) {
            alSourcef(pointer, AL_PITCH, value)
            field = value
        }

    var gain: Float = 1.0f
        set(value) {
            alSourcef(pointer, AL_GAIN, value)
            field = value
        }

    var position: Vector3fc = Vector3fConst.zero
        set(value) {
            alSource3f(pointer, AL_POSITION, value.x(), value.y(), value.z())
            field = value
        }

    var velocity: Vector3fc = Vector3fConst.zero
        set(value) {
            alSource3f(pointer, AL_VELOCITY, value.x(), value.y(), value.z())
            field = value
        }

    var looping: Boolean = false
        set(value) {
            alSourcei(pointer, AL_LOOPING, if (value) AL_TRUE else AL_FALSE)
            field = value
        }

    override fun play() {
        alSourcePlay(pointer)
    }

    override fun pause() {
        alSourcePause(pointer)
    }

    override fun stop() {
        alDeleteSources(pointer)
        //alSourceStop(pointer)
    }

    override var time: Float
        set(value) = alSourcef(pointer, AL_SEC_OFFSET, value)
        get() = alGetSourcef(pointer, AL_SEC_OFFSET)

    val state: PlayState
        get() = when (alGetSourcei(pointer, AL_SOURCE_STATE)) {
            AL_INITIAL -> PlayState.INITIAL
            AL_PLAYING -> PlayState.PLAYING
            AL_STOPPED -> PlayState.STOPPED
            AL_PAUSED -> PlayState.PAUSED
            else -> throw Exception("Unknown state")
        }

    override val playing: Boolean
        get() = state == PlayState.PLAYING

    override fun close() {
        alDeleteSources(pointer)
    }
}
