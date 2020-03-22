package graphics

import IDrawable
import org.lwjgl.opengl.GL41
import org.lwjgl.opengl.GL46.*


class Quad(private val data: FloatArray) : IDrawable {
    private val vbo: Int = glGenBuffers()
    private val vao: Int = glGenVertexArrays()
    private val shader: Shader =
        Shader("assets/shaders/fragment.glsl", "assets/shaders/vertex.glsl")

    init {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW)

        with(glGetAttribLocation(shader.program, "position")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 8 * 4, 0)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "z")) {
            glVertexAttribPointer(this, 1, GL_FLOAT, false, 8 * 4, 2 * 4)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "normal")) {
            glVertexAttribPointer(this, 3, GL_FLOAT, false, 8 * 4, 3 * 4)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "uv")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 8 * 4, 6 * 4)
            glEnableVertexAttribArray(this)
        }
        glBindVertexArray(0)
    }

    override fun draw() {
        glBindVertexArray(vao)
        shader.bind()
        glDrawArrays(GL_TRIANGLES, 0, data.size / 3)
        shader.unbind()
        glBindVertexArray(0)
    }
}