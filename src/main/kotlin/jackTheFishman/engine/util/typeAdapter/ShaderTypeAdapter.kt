package jackTheFishman.engine.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.engine.graphics.Shader
import jackTheFishman.engine.graphics.ShaderViaPath
import kotlin.reflect.KClass

class ShaderTypeAdapter : TypeAdapter<Shader> {
    override fun classFor(type: Any): KClass<out Shader> = when (type as String) {
        Shader::class.simpleName -> Shader::class
        ShaderViaPath::class.simpleName -> ShaderViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}