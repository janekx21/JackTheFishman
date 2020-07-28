package jackTheFishman.engine.util.typeAdapter

import com.beust.klaxon.TypeAdapter
import jackTheFishman.engine.graphics.CubeTexture
import jackTheFishman.engine.graphics.Texture
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.graphics.Texture2DViaPath
import kotlin.reflect.KClass

class TextureTypeAdapter : TypeAdapter<Texture> {
    override fun classFor(type: Any): KClass<out Texture> = when (type as String) {
        Texture2D::class.simpleName -> Texture2D::class
        CubeTexture::class.simpleName -> CubeTexture::class
        Texture2DViaPath::class.simpleName -> Texture2DViaPath::class
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}