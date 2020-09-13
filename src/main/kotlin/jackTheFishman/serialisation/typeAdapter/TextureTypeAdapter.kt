package jackTheFishman.serialisation.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.graphics.CubeTexture
import jackTheFishman.graphics.Texture
import jackTheFishman.graphics.Texture2D
import jackTheFishman.graphics.Texture2DViaPath
import kotlin.reflect.KClass

class TextureTypeAdapter : TypeAdapter<Texture> {
    override fun classFor(type: Any): KClass<out Texture> = when (type as String) {
        Texture2D::class.simpleName -> Texture2D::class
        CubeTexture::class.simpleName -> CubeTexture::class
        Texture2DViaPath::class.simpleName -> Texture2DViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}
