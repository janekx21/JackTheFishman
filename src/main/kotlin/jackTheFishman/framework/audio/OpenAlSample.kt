package jackTheFishman.framework.audio

import com.beust.klaxon.Json
import com.beust.klaxon.TypeFor
import jackTheFishman.framework.util.ICreateViaPath
import jackTheFishman.framework.util.IntPointer
import jackTheFishman.framework.util.typeAdapter.SampleTypeAdapter
import org.lwjgl.stb.STBVorbis
import org.lwjgl.system.MemoryStack
import java.io.Closeable

@TypeFor(field = "type", adapter = SampleTypeAdapter::class)
open class OpenAlSample(sampleFile: SampleFile) : Sample, Closeable {
    val type: String = javaClass.simpleName

    @Json(ignored = true)
    val pointer = OpenAl.genSample()

    init {
        OpenAl.setSampleData(pointer, sampleFile)
    }

    @Json(ignored = true)
    override val durationInSeconds = OpenAl.getSampleDurationInSeconds(pointer)

    override fun close() {
        OpenAl.deleteSample(pointer)
    }

    companion object : ICreateViaPath<OpenAlSample> {
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
                "Audio file could not be loaded at \"$path\". Error Nr. ${error.value}"
            }

            // Retrieve the extra information that was stored in the buffers by the function
            val channels = channelsBuffer.get()
            val sampleRate = sampleRateBuffer.get()

            // Free the space we allocated earlier
            MemoryStack.stackPop()
            MemoryStack.stackPop()

            return SampleFile(rawAudioBuffer, channels, sampleRate)
        }

        override fun createViaPath(path: String): OpenAlSample {
            return SampleViaPath(path)
        }
    }
}
