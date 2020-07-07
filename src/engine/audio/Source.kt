package engine.audio

import engine.math.Vector3fCopy
import engine.math.Vector3fExt
import engine.math.toJson
import engine.util.IJsonSerializable
import engine.util.IJsonUnserializable
import org.joml.Vector3f
import org.lwjgl.openal.AL10.*
import org.lwjgl.openal.AL11.AL_SEC_OFFSET
import java.io.Closeable

class Source(sample: Sample? = null) : Closeable, IPlayable, IJsonSerializable {
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

    override var time: Float
        set(value) = alSourcef(sourcePointer, AL_SEC_OFFSET, value)
        get() = alGetSourcef(sourcePointer, AL_SEC_OFFSET)

    private val alState: Int
        get() = alGetSourcei(sourcePointer, AL_SOURCE_STATE)

    override val playing: Boolean
        get() = alState == AL_PLAYING

    override fun close() {
        alDeleteSources(sourcePointer)
    }

    override fun toJson(): Any? {
        return mapOf(
            "sample" to sample?.toJson(),
            "pitch" to pitch,
            "gain" to gain,
            "position" to position.toJson(),
            "velocity" to velocity.toJson(),
            "looping" to looping,
            "time" to time,
            "alState" to alState
        )
    }

    companion object : IJsonUnserializable<Source> {
        override fun fromJson(json: Any?): Source {
            val map = json as Map<*, *>

            val source = Source(
                if (map["sample"] != null) {
                    Sample.fromJson(map["sample"])
                } else {
                    null
                }
            )

            source.pitch = (map["pitch"] as Double).toFloat()
            source.gain = (map["gain"] as Double).toFloat()
            source.position = Vector3fExt.fromJson(map["position"])
            source.velocity = Vector3fExt.fromJson(map["velocity"])
            source.looping = map["looping"] as Boolean

            when {
                map["alState"] == AL_PLAYING -> {
                    source.time = (map["time"] as Double).toFloat()
                    source.play()
                }
                map["alState"] == AL_PAUSED -> {
                    source.pause()
                    source.time = (map["time"] as Double).toFloat()
                }
                map["alState"] == AL_STOPPED -> {
                    source.stop()
                }
            }

            return source
        }
    }
}