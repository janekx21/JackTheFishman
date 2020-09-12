package jackTheFishman.framework.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.framework.audio.SampleViaPath
import jackTheFishman.framework.audio.openAl.OpenAlSample
import kotlin.reflect.KClass

class SampleTypeAdapter : TypeAdapter<OpenAlSample> {
    override fun classFor(type: Any): KClass<out OpenAlSample> = when (type as String) {
        OpenAlSample::class.simpleName -> OpenAlSample::class
        SampleViaPath::class.simpleName -> SampleViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
