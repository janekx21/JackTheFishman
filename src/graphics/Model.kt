package graphics

import linear.Location
import linear.Vector
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
        val positionAttr = glGetAttribLocation(shader.program, "position")
        glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(positionAttr)

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