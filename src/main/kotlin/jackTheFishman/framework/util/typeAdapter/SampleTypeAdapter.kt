package jackTheFishman.framework.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.framework.audio.Sample
import jackTheFishman.framework.audio.SampleViaPath
import kotlin.reflect.KClass

class SampleTypeAdapter : TypeAdapter<Sample> {
    override fun classFor(type: Any): KClass<out Sample> = when (type as String) {
        Sample::class.simpleName -> Sample::class
        SampleViaPath::class.simpleName -> SampleViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
