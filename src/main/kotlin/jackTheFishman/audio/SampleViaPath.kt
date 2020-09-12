package jackTheFishman.audio

import jackTheFishman.audio.openAl.OpenAlSample
import jackTheFishman.util.pointer.IntPointer
import org.lwjgl.stb.STBVorbis

class SampleViaPath(val path: String) : OpenAlSample(getSampleFileViaPath(path)) {
    companion object {
        fun getSampleFileViaPath(path: String): SampleFile {
            val channelsBuffer = IntPointer()
            val sampleRateBuffer = IntPointer()

            val rawAudioBuffer =
                STBVorbis.stb_vorbis_decode_filename(path, channelsBuffer.buffer, sampleRateBuffer.buffer)

            check(rawAudioBuffer != null) {
                val error = IntPointer()
                STBVorbis.stb_vorbis_open_filename(path, error.array, null)
                "Audio file at \"$path\" could not be loaded. Error Nr.: ${error.value}"
            }

            val channels = channelsBuffer.value
            val sampleRate = sampleRateBuffer.value

            return SampleFile(rawAudioBuffer, channels, sampleRate)
        }
    }
}
