package engine.graphics

import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_FLOAT

data class Vertex(val position: Vector3f, val uv: Vector2f, val normal: Vector3f) {

    data class Attribute(val name: String, val size: Int, val type: Int)

    fun toBuffer(): List<Float> {
        return listOf(position.x, position.y, position.z, uv.x, uv.y, normal.x, normal.y, normal.z)
    }

    companion object {
        private val attributes = arrayOf(
            Attribute("Position", 3, GL_FLOAT),
            Attribute("UV", 2, GL_FLOAT),
            Attribute("Normal", 3, GL_FLOAT)
        )

        fun vertexAttribute() {
            for (attribute in attributes.withIndex()) {
                with(attribute.index) {
                    GL46.glVertexAttribPointer(
                        this, attribute.value.size, attribute.value.type,
                        false, size(), attribute.index.toLong() * 4
                    )
                    GL46.glEnableVertexAttribArray(this)
                }
            }
        }

        fun namedIndex(): Array<String> {
            return attributes.map { attribute -> attribute.name }.toTypedArray()
        }

        private fun size(): Int {
            return attributes.sumBy { attribute -> attribute.size } * 4
        }
    }
}