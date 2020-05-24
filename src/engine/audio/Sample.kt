package engine.audio

import engine.math.Vector3fCopy
import engine.util.ICreateViaPath
import org.joml.Vector3f
import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis
import org.lwjgl.system.MemoryStack
import java.io.Closeable
import java.nio.ShortBuffer

class Sample(rawAudioBuffer: ShortBuffer, channels: Int, sampleRate: Int) : Closeable {

    // Find the correct OpenAL format
    private val format = formats[channels]

    //Request space for the buffer
    private val bufferPointer = alGenBuffers()

    init {
        check(format != null) { "format not found" }
        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate)
    }

    private val sourcePointer = alGenSources()

    init {
        alSourcei(sourcePointer, AL_BUFFER, bufferPointer)
        //    AL10.alSourcef(sourcePointer, AL10.AL_PITCH, .3f)
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

    companion object : ICreateViaPath<Sample> {
        val formats = mapOf(1 to AL_FORMAT_MONO16, 2 to AL_FORMAT_STEREO16)

        override fun createViaPath(path: String): Sample {
            // Allocate space to store return information from the function
            MemoryStack.stackPush()

            val channelsBuffer = MemoryStack.stackMallocInt(1)

            MemoryStack.stackPush()

            val sampleRateBuffer = MemoryStack.stackMallocInt(1)
            val rawAudioBuffer =
                STBVorbis.stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer)
            check(rawAudioBuffer != null) { "audio file could not be loaded at $path" }

            // Retrieve the extra information that was stored in the buffers by the function
            val channels = channelsBuffer.get()
            val sampleRate = sampleRateBuffer.get()

            //Free the space we allocated earlier
            MemoryStack.stackPop()
            MemoryStack.stackPop()

            return Sample(rawAudioBuffer, channels, sampleRate)
        }
    }
}