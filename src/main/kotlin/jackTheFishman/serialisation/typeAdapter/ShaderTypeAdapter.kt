package jackTheFishman.serialisation.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.graphics.Shader
import jackTheFishman.graphics.ShaderViaPath
import kotlin.reflect.KClass

class ShaderTypeAdapter : TypeAdapter<Shader> {
    override fun classFor(type: Any): KClass<out Shader> = when (type as String) {
        Shader::class.simpleName -> Shader::class
        ShaderViaPath::class.simpleName -> ShaderViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
