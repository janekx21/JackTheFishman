package jackTheFishman.framework.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.framework.audio.OpenAlSample
import jackTheFishman.framework.audio.SampleViaPath
import kotlin.reflect.KClass

class SampleTypeAdapter : TypeAdapter<OpenAlSample> {
    override fun classFor(type: Any): KClass<out OpenAlSample> = when (type as String) {
        OpenAlSample::class.simpleName -> OpenAlSample::class
        SampleViaPath::class.simpleName -> SampleViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
