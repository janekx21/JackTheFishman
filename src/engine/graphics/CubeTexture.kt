package engine.graphics

import engine.util.ICreateViaPath
import engine.util.IUsable
import engine.util.IntPointer
import org.joml.Vector2i
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load
import java.io.File
import java.nio.ByteBuffer

class CubeTexture(val size: Vector2i) : Texture(), IUsable {
    private val texture = glGenTextures()

    init {
        glEnable(GL_TEXTURE_CUBE_MAP)
    }

    fun setData(data: Array<ByteBuffer>) {
        check(data.size == 6) { "CubeTexture needs 6 images" }
        use {
            for (i in 0..5) {
                glTexImage2D(
                    GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0,
                    GL_RGBA,
                    size.x,
                    size.y,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    data[i]
                )
            }
            glGenerateMipmap(GL_TEXTURE_CUBE_MAP)
        }
    }

    override fun bindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture)
    }

    override fun unbindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0)
    }

    override fun use(callback: () -> Unit) {
        bindWithIndex(0)
        callback()
        unbindWithIndex(0)
    }

    companion object : ICreateViaPath<CubeTexture> {
        private val fileNames = arrayOf("right", "left", "top", "bottom", "front", "back")

        override fun createViaPath(path: String): CubeTexture {
            val parentDirectory = File(path)
            check(parentDirectory.isDirectory) { "CubeTexture loading needs a directory" }

            val list = arrayListOf<ByteBuffer>()
            var size: Vector2i? = null
            for (i in 0..5) {
                val file = File(parentDirectory, "${fileNames[i]}.png")

                val width = IntPointer()
                val height = IntPointer()
                val channels = IntPointer()
                stbi_set_flip_vertically_on_load(true)
                val data = stbi_load(file.path, width.buffer, height.buffer, channels.buffer, 4)
                check(data != null) { "image could not be loaded" }
                if (size != null) {
                    // TODO check if this is really needed
                    check(size.equals(width.value, height.value)) { "all textures need to be the same size" }
                }
                size = Vector2i(width.value, height.value)
                list.add(data)
            }
            val texture = CubeTexture(size!!)
            texture.setData(list.toTypedArray())

            for (data in list) {
                STBImage.stbi_image_free(data)
            }

            return texture
        }
    }
}