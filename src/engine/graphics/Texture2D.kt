package engine.graphics

import engine.util.ICreateViaPath
import engine.util.IUsable
import engine.util.IntPointer
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer

class Texture2D(val size: Vector2ic) : Texture(), IUsable {
    val texture = glGenTextures()
    private var internalData = ByteArray(0)

    init {
        glEnable(GL_TEXTURE_2D)
    }

    fun setData(data: ByteBuffer) {
        use {
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGBA,
                size.x(),
                size.y(),
                0,
                GL_RGBA,
                GL_UNSIGNED_BYTE,
                data
            )
            glGenerateMipmap(GL_TEXTURE_2D)
        }

        internalData = ByteArray(data.remaining())
        data.get(internalData)
        data.rewind()
    }

    fun fillWithNull() {
        use {
            val n: ByteBuffer? = null
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size.x(), size.y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, n)
        }
    }

    fun fillWithZeroDepth() {
        use {
            val n: ByteBuffer? = null
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_DEPTH24_STENCIL8,
                size.x(),
                size.y(),
                0,
                GL_DEPTH_STENCIL,
                GL_UNSIGNED_INT_24_8,
                n
            )
        }
    }

    fun makeLinear() {
        use {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        }
    }

    fun toGLFWImage(): GLFWImage {
        val image = GLFWImage.create()
        val bb = BufferUtils.createByteBuffer(size.x() * size.y() * 4)
        // copy and flip the image
        for (i in 0 until size.x() * size.y() * 4) {
            val x = i / 4 % size.x()
            val y = size.y() - 1 - i / 4 / size.x() // flip y
            val b = i % 4

            bb.put(i, internalData[(x + y * size.x()) * 4 + b])
        }

        image.set(size.x(), size.y(), bb)
        return image
    }

    override fun bindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, texture)
    }

    override fun unbindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    override fun use(callback: () -> Unit) {
        bindWithIndex(0)
        callback()
        unbindWithIndex(0)
    }

    companion object : ICreateViaPath<Texture2D> {
        override fun createViaPath(path: String): Texture2D {
            val width = IntPointer()
            val height = IntPointer()
            val channels = IntPointer()
            stbi_set_flip_vertically_on_load(true)
            val data = stbi_load(path, width.buffer, height.buffer, channels.buffer, 4)
            check(data != null) { "image could not be loaded" }
            val size = Vector2i(width.value, height.value)

            val texture = Texture2D(size)
            texture.setData(data)

            stbi_image_free(data)
            return texture
        }
    }
}