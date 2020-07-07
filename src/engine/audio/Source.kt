package engine.audio

import engine.math.Vector3fCopy
import org.joml.Vector3f
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.AL11.AL_SEC_OFFSET
import java.io.Closeable

class Source(sample: Sample? = null) : Closeable, IPlayable {
    private val sourcePointer = alGenSources()
    var sample: Sample? = sample
        set(value) {
            if (value != null) {
                alSourcei(sourcePointer, AL_BUFFER, value.bufferPointer)
            }
            field = value
        }

    var pitch: Float = 1.0f
        set(value) {
            alSourcef(sourcePointer, AL_PITCH, value)
            field = value
        }

    var gain: Float = 1.0f
        set(value) {
            alSourcef(sourcePointer, AL_GAIN, value)
            field = value
        }

    var position: Vector3f = Vector3fCopy.zero
        set(value) {
            alSource3f(sourcePointer, AL_POSITION, value.x, value.y, value.z)
            field = value
        }

    var velocity: Vector3f = Vector3fCopy.zero
        set(value) {
            alSource3f(sourcePointer, AL_VELOCITY, value.x, value.y, value.z)
            field = value
        }

    var looping: Boolean = false
        set(value) {
            alSourcei(sourcePointer, AL_LOOPING, if (value) AL_TRUE else AL_FALSE)
            field = value
        }

    override fun play() {
        alSourcePlay(sourcePointer)
    }

    override fun pause() {
        alSourcePause(sourcePointer)
    }

    override fun stop() {
        alSourceStop(sourcePointer)
    }

    override val time: Float
        get() = alGetSourcef(sourcePointer, AL_SEC_OFFSET)
    override val playing: Boolean
        get() = alGetSourcei(sourcePointer, AL_SOURCE_STATE) == AL_PLAYING

    override fun close() {
        alDeleteSources(sourcePointer)
    }
}