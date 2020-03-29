package graphics

import math.Point
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*

class Texture(private val path: String, private val index: Int = 0) {
    private val size: Point
    private val texture: Int

    init {
        val width = intArrayOf(0)
        val height = intArrayOf(0)
        val channels = intArrayOf(0)
        stbi_set_flip_vertically_on_load(true)
        val data = stbi_load(path, width, height, channels, 4)
        check(data != null) { "image could not be loaded" }
        size = Point(width[0], height[0])

        glActiveTexture(GL_TEXTURE0 + index)
        glEnable(GL_TEXTURE_2D)

        texture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texture)
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
        glBindTexture(GL_TEXTURE_2D, 0)

        stbi_image_free(data)
    }

    fun bind() {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, texture)
    }
}