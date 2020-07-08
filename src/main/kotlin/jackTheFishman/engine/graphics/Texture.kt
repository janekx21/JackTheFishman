package jackTheFishman.engine.graphics

abstract class Texture {
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