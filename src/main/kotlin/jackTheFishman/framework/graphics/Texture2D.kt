package jackTheFishman.framework.graphics

import com.beust.klaxon.Json
import jackTheFishman.framework.util.ICreateViaPath
import jackTheFishman.framework.util.IUsable
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL46.*
import java.nio.ByteBuffer

open class Texture2D : Texture(), IUsable {
    @Json(ignored = true)
    override val pointer = glGenTextures()

    @Json(ignored = true)
    var size: Vector2ic = Vector2i(0, 0)
        private set

    @Json(ignored = true)
    private var internalData = ByteArray(0)

    init {
        glEnable(GL_TEXTURE_2D)
    }

    fun setData(data: ByteBuffer, size: Vector2ic) {
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
            // is needed because of the texture filtering mode
            glGenerateMipmap(GL_TEXTURE_2D)
        }

        // copy's data to internalData
        internalData = ByteArray(data.remaining())
        data.get(internalData)
        data.rewind()
        this.size = Vector2i(size)
    }

    fun fillWithNull(size: Vector2ic) {
        this.size = Vector2i(size)
        use {
            val n: ByteBuffer? = null
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.size.x(), this.size.y(), 0, GL_RGBA, GL_UNSIGNED_BYTE, n)
        }
    }

    fun fillWithZeroDepth(size: Vector2ic) {
        this.size = Vector2i(size)
        use {
            val n: ByteBuffer? = null
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_DEPTH24_STENCIL8,
                this.size.x(),
                this.size.y(),
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

    fun asGLFWImage(): GLFWImage {
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
        glBindTexture(GL_TEXTURE_2D, pointer)
    }

    override fun unbindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    /**
     * Binds and unbinds the texture.
     * This wraps the callback in a bound state.
     */
    override fun use(callback: () -> Unit) {
        bindWithIndex(0)
        callback()
        unbindWithIndex(0)
    }

    companion object : ICreateViaPath<Texture2D> {
        override fun createViaPath(path: String): Texture2DViaPath {
            return Texture2DViaPath(path)
        }

        fun setDefaultTexture2DWhite() {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                1,
                1,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                intArrayOf(0xffffff)
            )
        }
    }
}
