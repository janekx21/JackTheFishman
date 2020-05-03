package engine.graphics

import engine.util.IntPointer
import org.joml.Vector2i
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer

class Texture(private val size: Vector2i) {
    private val texture = glGenTextures()

    init {
        glEnable(GL_TEXTURE_2D)
    }

    fun setData(data: ByteBuffer): Unit {
        bind {
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                size.x,
                size.y,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            )
            glGenerateMipmap(GL_TEXTURE_2D)
        }
    }

    fun bindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, texture)
    }

    fun bind(callback: () -> Unit) {
        bindWithIndex(0)
        callback()
        unbindWithIndex(0)
    }

    companion object {
        fun createViaPath(path: String): Texture {
            val width = IntPointer()
            val height = IntPointer()
            val channels = IntPointer()
            stbi_set_flip_vertically_on_load(true)
            val data = stbi_load(path, width.buffer, height.buffer, channels.buffer, 4)
            check(data != null) { "image could not be loaded" }
            val size = Vector2i(width.value, height.value)

            val texture = Texture(size)
            texture.setData(data)

            stbi_image_free(data)
            return texture
        }

        private fun unbindWithIndex(index: Int) {
            glActiveTexture(GL_TEXTURE0 + index)
            glBindTexture(GL_TEXTURE_2D, 0)
        }

        fun bind(textures: Array<Texture>, callback: () -> Unit) {
            for (texture in textures.withIndex()) {
                texture.value.bindWithIndex(texture.index)
            }

            callback()

            for (texture in textures.withIndex()) {
                unbindWithIndex(texture.index)
            }
        }
    }
}