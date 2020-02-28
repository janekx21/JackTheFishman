import org.lwjgl.opengl.GL46.*

class VertexBuffer(mesh: Mesh) : IBindable, IDrawable {
    private val buffer = glGenBuffers()
    private val size: Int = mesh.size

    init {
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
        glBufferData(GL_ARRAY_BUFFER, mesh.toFloatArray(), GL_STATIC_DRAW)
    }

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, buffer)
    }

    override fun draw() {
        glDrawArrays(GL_TRIANGLES, 0, size)
    }
}