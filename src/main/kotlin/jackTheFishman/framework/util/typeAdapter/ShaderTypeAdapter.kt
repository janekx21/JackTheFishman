package jackTheFishman.framework.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.framework.graphics.Shader
import jackTheFishman.framework.graphics.ShaderViaPath
import kotlin.reflect.KClass

class ShaderTypeAdapter : TypeAdapter<Shader> {
    override fun classFor(type: Any): KClass<out Shader> = when (type as String) {
        Shader::class.simpleName -> Shader::class
        ShaderViaPath::class.simpleName -> ShaderViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
