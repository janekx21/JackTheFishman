package engine.audio

import engine.math.Vector3fCopy
import org.joml.Vector3f
import org.lwjgl.openal.AL10.*
import java.io.Closeable

class Source(sample: Sample) : Closeable {
    private val sourcePointer = alGenSources()

    init {
        alSourcei(sourcePointer, AL_BUFFER, sample.bufferPointer)
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

    fun play() {
        alSourcePlay(sourcePointer)
    }

    override fun close() {
        alDeleteSources(sourcePointer)
    }
}