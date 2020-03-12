import org.lwjgl.opengl.GL46.*


class Quad(data: FloatArray) : IDrawable {
    private val vbo: Int = glGenBuffers()
    private val vao: Int = glGenVertexArrays()
    private val shader: Shader = Shader("assets/shaders/fragment.glsl", "assets/shaders/vertex.glsl")

    init {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)
        val positionAttr = glGetAttribLocation(shader.program, "position")
        glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(positionAttr)
        glBindVertexArray(0)
    }

    override fun draw() {
        glBindVertexArray(vao)
        shader.bind()
        glDrawArrays(GL_TRIANGLES, 0, 6)
        shader.unbind()
        glBindVertexArray(0)
    }
}