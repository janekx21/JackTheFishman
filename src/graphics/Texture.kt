package graphics

import IBindableViaIndex
import math.Point2
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer

class Texture : IBindableViaIndex {
    val size: Point2
    val texture = glGenTextures()

    init {
        glEnable(GL_TEXTURE_2D)
    }

    constructor(data: ByteBuffer?, size: Point2) {
        this.size = size
        bind(0) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
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

    constructor(size: Point2) {
        this.size = size
        bind(0) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                size.x,
                size.y,
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                0
            )
        }
    }

    companion object {
        val indexBound = BooleanArray(maxTextureUnits()) { false }

        fun fromPath(path: String): Texture {
            val width = intArrayOf(0)
            val height = intArrayOf(0)
            val channels = intArrayOf(0)
            stbi_set_flip_vertically_on_load(true)
            val data = stbi_load(path, width, height, channels, 4)
            check(data != null) { "image could not be loaded" }
            val size = Point2(width[0], height[0])
            val tex = Texture(data, size)
            stbi_image_free(data)
            return tex
        }

        private fun maxTextureUnits(): Int {
            return glGetInteger(GL13.GL_MAX_TEXTURE_UNITS).also { println("you can do $it texture units") }
        }
    }


    override fun bind(index: Int, callback: () -> Unit) {
        check(index >= 0 && index < indexBound.size) { "index can not be bound" }
        check(!indexBound[index]) { "index can only be bound once" }
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, texture)
        indexBound[index] = true
        callback.invoke()
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, 0)
        indexBound[index] = false
    }
}