package engine.audio

import engine.util.ICreateViaPath
import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis
import org.lwjgl.system.MemoryStack
import java.nio.ShortBuffer

class Sample(rawAudioBuffer: ShortBuffer, channels: Int, sampleRate: Int) {

    // Find the correct OpenAL format
    private val format = formats[channels]

    //Request space for the buffer
    val bufferPointer = alGenBuffers()

    init {
        check(format != null) { "format not found" }
        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate)
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