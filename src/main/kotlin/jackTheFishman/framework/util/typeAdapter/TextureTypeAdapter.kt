package jackTheFishman.framework.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.framework.graphics.CubeTexture
import jackTheFishman.framework.graphics.Texture
import jackTheFishman.framework.graphics.Texture2D
import jackTheFishman.framework.graphics.Texture2DViaPath
import kotlin.reflect.KClass

class TextureTypeAdapter : TypeAdapter<Texture> {
    override fun classFor(type: Any): KClass<out Texture> = when (type as String) {
        Texture2D::class.simpleName -> Texture2D::class
        CubeTexture::class.simpleName -> CubeTexture::class
        Texture2DViaPath::class.simpleName -> Texture2DViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
