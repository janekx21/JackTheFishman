package graphics

import linear.Vector
import org.lwjgl.opengl.GL46

data class Vertex(val pos: Vector, val uv: Vector) {

    fun toArray(): List<Float> {
        return listOf(pos.x, pos.y, uv.x, uv.y)
    }

    companion object {
        fun generateVertexAttrib(shader: Shader) {
            with(GL46.glGetAttribLocation(shader.program, "position")) {
                GL46.glVertexAttribPointer(this, 2, GL46.GL_FLOAT, false, Int.SIZE_BYTES * 4, 0)
                GL46.glEnableVertexAttribArray(this)
            }

            with(GL46.glGetAttribLocation(shader.program, "uv")) {
                GL46.glVertexAttribPointer(
                    this,
                    2,
                    GL46.GL_FLOAT,
                    false,
                    Int.SIZE_BYTES * 4,
                    Int.SIZE_BYTES.toLong() * 2
                )
                GL46.glEnableVertexAttribArray(this)
            }

        }
    }
}