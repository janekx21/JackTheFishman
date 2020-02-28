import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*

class Quad() : IDrawable {
    private val buffer: Int = glGenBuffers()
    private val shader: Shader = Shader("assets/shaders/fragment.glsl", "assets/shaders/vertex.glsl")

    init {
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
        glBufferData(GL_ARRAY_BUFFER, floatArrayOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f, 1f, 1f), GL_STATIC_DRAW)
        val positionAttr = glGetAttribLocation(shader.program, "position")
        glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(positionAttr)
        val width = intArrayOf(0)
        val height = intArrayOf(0)
        val channels = intArrayOf(0)
        stbi_set_flip_vertically_on_load(true)
        val data = stbi_load("assets/ex.png", width, height, channels, 4)
        check(data != null) { "image could not be loaded" }
        val texture = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, texture)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data)
        glGenerateMipmap(GL_TEXTURE_2D)
        stbi_image_free(data)
    }

    override fun draw() {
        glBindBuffer(GL_ARRAY_BUFFER, buffer)

        shader.bind()
        glDrawArrays(GL_TRIANGLES, 0, 6)
    }
}