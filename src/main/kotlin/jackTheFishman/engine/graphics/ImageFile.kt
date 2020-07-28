package jackTheFishman.engine.graphics

import jackTheFishman.engine.util.IntPointer
import org.joml.Vector2i
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load
import java.io.Closeable

class ImageFile(path: String) : Closeable {
    private val width = IntPointer()
    private val height = IntPointer()
    val channels = IntPointer()

    init {
        stbi_set_flip_vertically_on_load(true)
    }

    private val possiblyData = STBImage.stbi_load(path, width.buffer, height.buffer, channels.buffer, 4)

    init {
        check(possiblyData != null) { "image could not be loaded at path: $path" }
    }

    val data = possiblyData!!

    val size = Vector2i(width.value, height.value)

    override fun close() {
        STBImage.stbi_image_free(data)
    }
}