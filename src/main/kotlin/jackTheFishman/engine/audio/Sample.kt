package jackTheFishman.engine.audio

import jackTheFishman.engine.util.ICreateViaPath
import jackTheFishman.engine.util.IJsonSerializable
import jackTheFishman.engine.util.IJsonUnserializable
import jackTheFishman.engine.util.IntPointer
import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis
import org.lwjgl.system.MemoryStack
import java.nio.ShortBuffer

class Sample(rawAudioBuffer: ShortBuffer, channels: Int, sampleRate: Int, private val path: String? = null) : IJsonSerializable {

    // Find the correct OpenAL format
    private val format = formats[channels]

    //Request space for the buffer
    val bufferPointer = alGenBuffers()

    init {
        check(format != null) { "format not found" }
        //Send the data to OpenAL
        alBufferData(bufferPointer, format, rawAudioBuffer, sampleRate)
    }

    override fun toJson(): Any? {
        checkNotNull(path)

        return mapOf(
            "path" to path
        )
    }

    companion object : ICreateViaPath<Sample>, IJsonUnserializable<Sample> {
        val formats = mapOf(1 to AL_FORMAT_MONO16, 2 to AL_FORMAT_STEREO16)

        override fun createViaPath(path: String): Sample {
            // Allocate space to store return information from the function
            MemoryStack.stackPush()

            val channelsBuffer = MemoryStack.stackMallocInt(1)

            MemoryStack.stackPush()

            val sampleRateBuffer = MemoryStack.stackMallocInt(1)
            val rawAudioBuffer =
                STBVorbis.stb_vorbis_decode_filename(path, channelsBuffer, sampleRateBuffer)
            check(rawAudioBuffer != null) {
                val error = IntPointer()
                STBVorbis.stb_vorbis_open_filename(path, error.buffer, null)
                "audio file could not be loaded at \"$path\". error Nr. ${error.value}"
            }

            // Retrieve the extra information that was stored in the buffers by the function
            val channels = channelsBuffer.get()
            val sampleRate = sampleRateBuffer.get()

            //Free the space we allocated earlier
            MemoryStack.stackPop()
            MemoryStack.stackPop()

            return Sample(rawAudioBuffer, channels, sampleRate)
        }

        override fun fromJson(json: Any?): Sample {
            val map = json as Map<*, *>

            checkNotNull(map["path"])

            return createViaPath(map["path"] as String)
        }
    }
}