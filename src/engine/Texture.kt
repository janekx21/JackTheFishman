package engine

import engine.math.Point
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*
import engine.util.IntPointer

class Texture(private val path: String, private val index: Int = 0) {
    private val size: Point
    private val texture: Int

    init {
        val width = IntPointer()
        val height = IntPointer()
        val channels = IntPointer()
        stbi_set_flip_vertically_on_load(true)
        val data = stbi_load(path, width.buffer, height.buffer, channels.buffer, 4)
        check(data != null) { "image could not be loaded" }
        size = Point(width.value, height.value)

        glActiveTexture(GL_TEXTURE0 + index)
        glEnable(GL_TEXTURE_2D)

        texture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texture)
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
        glBindTexture(GL_TEXTURE_2D, 0)

        stbi_image_free(data)
    }

    fun bind() {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, texture)
    }
}