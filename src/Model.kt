import org.lwjgl.opengl.GL46.*

open class Model(
    private val buffer: VertexBuffer,
    private val shader: Shader,
    private var position: Vector = Vector(0f, 0f),
    private val texture: Texture? = null
) : IDrawable {
    private val posAttr: Int

    constructor(
        mesh: Mesh, shader: Shader,
        position: Vector = Vector(0f, 0f),
        texture: Texture? = null
    ) : this(mesh.toVertexBuffer(), shader, position, texture) {}

    init {
        val positionAttr = glGetAttribLocation(shader.program, "position")
        glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(positionAttr)

        posAttr = glGetUniformLocation(shader.program, "pos")

        if (texture != null) {
            val texAttr = glGetAttribLocation(shader.program, "tex")
            glProgramUniform1i(shader.program, texAttr, 0)
        }
    }

    override fun draw() {

        texture?.bind()
        shader.bind()
        glUniform2f(posAttr, position.x, position.y)

        buffer.draw()
    }
}