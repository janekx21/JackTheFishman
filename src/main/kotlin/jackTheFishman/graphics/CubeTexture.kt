package jackTheFishman.graphics

import com.beust.klaxon.Json
import jackTheFishman.util.CreateViaPath
import jackTheFishman.util.Usable
import jackTheFishman.util.pointer.IntPointer
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImage.stbi_load
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load
import java.io.File
import java.nio.ByteBuffer

class CubeTexture : Texture(), Usable {
    @Json(ignored = true)
    override val pointer = glGenTextures()
    private var internalSize: Vector2ic = Vector2i(0, 0)

    init {
        glEnable(GL_TEXTURE_CUBE_MAP)
    }

    fun setData(data: Array<ByteBuffer>, size: Vector2ic) {
        check(data.size == 6) { "CubeTexture needs 6 images" }
        use {
            for (i in 0..5) {
                glTexImage2D(
                    GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                    0,
                    GL_RGBA,
                    size.x(),
                    size.y(),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    data[i]
                )
            }
            glGenerateMipmap(GL_TEXTURE_CUBE_MAP)
        }
        internalSize = Vector2i(size)
    }

    override fun bindWithIndex(index: Int) {
        glActiveTexture(GL_TEXTURE0 + index)
        glBindTexture(GL_TEXTURE_CUBE_MAP, pointer)
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

    companion object : CreateViaPath<CubeTexture> {
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
                val data = stbi_load(file.path, width.array, height.array, channels.array, 4)
                check(data != null) { "image could not be loaded" }
                if (size != null) {
                    // TODO check if this is really needed
                    check(size.equals(width.value, height.value)) { "all textures need to be the same size" }
                }
                size = Vector2i(width.value, height.value)
                list.add(data)
            }
            val texture = CubeTexture()
            texture.setData(list.toTypedArray(), size!!)

            for (data in list) {
                STBImage.stbi_image_free(data)
            }

            return texture
        }
    }
}
