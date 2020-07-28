package jackTheFishman.engine.audio

import com.beust.klaxon.Json
import com.beust.klaxon.TypeFor
import jackTheFishman.engine.util.ICreateViaPath
import jackTheFishman.engine.util.IntPointer
import jackTheFishman.engine.util.typeAdapter.SampleTypeAdapter
import org.lwjgl.openal.AL10.*
import org.lwjgl.stb.STBVorbis
import org.lwjgl.system.MemoryStack

@TypeFor(field = "type", adapter = SampleTypeAdapter::class)
open class Sample(sampleFile: SampleFile) {
    val type: String = javaClass.simpleName

    // Find the correct OpenAL format
    private val format = formats[sampleFile.channelCount]

    //Request space for the buffer
    @Json(ignored = true)
    val pointer = alGenBuffers()

    init {
        check(format != null) { "format not found" }
        //Send the data to OpenAL
        alBufferData(pointer, format, sampleFile.data, sampleFile.sampleRate)
    }

    companion object : ICreateViaPath<Sample> {
        val formats = mapOf(1 to AL_FORMAT_MONO16, 2 to AL_FORMAT_STEREO16)

        fun getSampleFileViaPath(path: String): SampleFile {
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
            return SampleFile(rawAudioBuffer, channels, sampleRate)
        }

        override fun createViaPath(path: String): Sample {
            return SampleViaPath(path)
        }
    }
}