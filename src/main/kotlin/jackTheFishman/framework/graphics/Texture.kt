package jackTheFishman.framework.graphics

import com.beust.klaxon.TypeFor
import jackTheFishman.framework.util.typeAdapter.TextureTypeAdapter

@TypeFor(field = "type", adapter = TextureTypeAdapter::class)
abstract class Texture {
    val type: String = javaClass.simpleName

    abstract val pointer: Int
    abstract fun bindWithIndex(index: Int)
    abstract fun unbindWithIndex(index: Int)

    companion object {
        fun bind(textures: Array<Texture>, callback: () -> Unit) {
            for (texture in textures.withIndex()) {
                texture.value.bindWithIndex(texture.index)
            }

            callback()

            for (texture in textures.withIndex()) {
                texture.value.unbindWithIndex(texture.index)
            }
        }

    }
}
