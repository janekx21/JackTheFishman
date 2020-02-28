import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*

class Texture(private val path: String) : IBindable {
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
        texture = glGenTextures()
        glActiveTexture(GL_TEXTURE0)
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

        stbi_image_free(data)
    }


    override fun bind() {
        glBindTexture(GL_TEXTURE_2D, texture)
    }
}