package graphics

import linear.Location
import linear.Vector
import org.lwjgl.opengl.GL46.*

open class Model(
    private val buffer: VertexBuffer,
    private val shader: Shader,
    private var location: Location = Location.identity,
    private val texture: Texture? = null
) : IDrawable {
    private val posAttr: Int
    private val scaleAttr: Int

    init {
        buffer.bind()
        with(glGetAttribLocation(shader.program, "position")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 4 * 4, 0)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "uv")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, 4 * 4, 2 * 4)
            glEnableVertexAttribArray(this)
        }

        // Vertex.generateVertexAttrib(shader)

        posAttr = glGetUniformLocation(shader.program, "pos")
        scaleAttr = glGetUniformLocation(shader.program, "scale")

        if (texture != null) {
            val texAttr = glGetAttribLocation(shader.program, "tex")
            glProgramUniform1i(shader.program, texAttr, 0)
        }
    }

    override fun draw() {
        texture?.bind()
        shader.bind()

        buffer.draw()

        glUseProgram(0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    public fun setPosition(value: Vector) {
        location.position = value
        glProgramUniform2f(shader.program, posAttr, location.position.x, location.position.y)
    }

    public fun setScale(value: Vector) {
        location.scale = value
        glProgramUniform2f(shader.program, scaleAttr, location.scale.x, location.scale.y)
    }
}