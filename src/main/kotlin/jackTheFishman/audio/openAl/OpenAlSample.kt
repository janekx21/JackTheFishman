package jackTheFishman.audio.openAl

import com.beust.klaxon.Json
import com.beust.klaxon.TypeFor
import jackTheFishman.audio.Sample
import jackTheFishman.audio.SampleFile
import jackTheFishman.audio.SampleViaPath
import jackTheFishman.serialisation.typeAdapter.SampleTypeAdapter
import jackTheFishman.util.CreateViaPath
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

    companion object : CreateViaPath<OpenAlSample> {
        override fun createViaPath(path: String): OpenAlSample {
            return SampleViaPath(path)
        }
    }
}
