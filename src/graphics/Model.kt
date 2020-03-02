package graphics

import linear.Location
import linear.Vector
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.*

open class Model(
    private val buffer: VertexBuffer,
    private val shader: Shader,
    private var location: Location = Location.zero,
    private val texture: Texture? = null
) : IDrawable {
    private val posAttr: Int
    private val scaleAttr: Int

    constructor(
        mesh: Mesh, shader: Shader,
        position: Vector = Vector(0f, 0f),
        texture: Texture? = null
    ) : this(mesh.toVertexBuffer(), shader, Location(position, 0f, Vector.zero), texture) {
    }

    init {
        with(glGetAttribLocation(shader.program, "position")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, Int.SIZE_BYTES*4, 0)
            glEnableVertexAttribArray(this)
        }

        with(glGetAttribLocation(shader.program, "uv")) {
            glVertexAttribPointer(this, 2, GL_FLOAT, false, Int.SIZE_BYTES*4, Int.SIZE_BYTES.toLong()*2)
            glEnableVertexAttribArray(this)
        }

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