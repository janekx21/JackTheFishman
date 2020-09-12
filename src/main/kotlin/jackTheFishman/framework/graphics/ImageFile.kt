package jackTheFishman.framework.graphics

import jackTheFishman.framework.util.IntPointer
import org.joml.Vector2i
import org.joml.Vector2ic
import org.lwjgl.stb.STBImage
import org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load
import java.io.Closeable

/**
 * Represents a image file as buffer object.
 * Primary for loading `jpeg` and `png` image files.
 */
class ImageFile(path: String) : Closeable {
    private val width = IntPointer()
    private val height = IntPointer()
    private val channels = IntPointer()

    init {
        stbi_set_flip_vertically_on_load(true)
    }

    private val possiblyData = STBImage.stbi_load(path, width.array, height.array, channels.array, 4)

    init {
        check(possiblyData != null) { "Image could not be loaded at path: $path" }
    }

    val data = possiblyData!!

    val size: Vector2ic = Vector2i(width.value, height.value)

    override fun close() {
        STBImage.stbi_image_free(data)
    }
}
