package jackTheFishman.engine.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.SampleViaPath
import kotlin.reflect.KClass

class SampleTypeAdapter : TypeAdapter<Sample> {
    override fun classFor(type: Any): KClass<out Sample> = when (type as String) {
        Sample::class.simpleName -> Sample::class
        SampleViaPath::class.simpleName -> SampleViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}