import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL46.*


class Quad(): IDrawable {
    private val buffer: Int = glGenBuffers()
    private val shader: Shader = Shader("assets/shaders/fragment.glsl","assets/shaders/vertex.glsl")
    init{
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
        glBufferData(GL_ARRAY_BUFFER, floatArrayOf(0f,0f,0f,1f,1f,1f,1f,0f), GL_STATIC_DRAW)
        val positionAttr = glGetAttribLocation(shader.program, "position")
        glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0)
        glEnableVertexAttribArray(positionAttr)


    }

    override fun draw() {
        glBindBuffer(GL_ARRAY_BUFFER, buffer)

        shader.bind()
        glDrawArrays(GL11.GL_QUADS, 0, 4)

    }
}