package jackTheFishman.framework.graphics

import org.joml.Vector2fc
import org.joml.Vector3fc
import org.lwjgl.opengl.GL46.*

data class Vertex(val position: Vector3fc, val uv: Vector2fc, val normal: Vector3fc, val tangent: Vector3fc) {

    data class Attribute(val name: String, val size: Int, val type: Int)

    fun asList(): List<Float> {
        return listOf(
            position.x(),
            position.y(),
            position.z(),
            uv.x(),
            uv.y(),
            normal.x(),
            normal.y(),
            normal.z(),
            tangent.x(),
            tangent.y(),
            tangent.z()
        )
    }

    companion object {
        private val attributes = arrayOf(
            Attribute("Position", 3, GL_FLOAT),
            Attribute("UV", 2, GL_FLOAT),
            Attribute("Normal", 3, GL_FLOAT),
            Attribute("Tangent", 3, GL_FLOAT)
        )
        private const val floatSize = 4

        fun generateVertexAttributePointers() {
            var stride = 0
            for (attribute in attributes.withIndex()) {
                with(attribute.index) {
                    createVertexAttribute(this, attribute.value, stride.toLong())
                    stride += attribute.value.size * floatSize // only upload floats
                }
            }
        }

        private fun createVertexAttribute(index: Int, attribute: Attribute, stride: Long) {
            glVertexAttribPointer(
                index, attribute.size, attribute.type,
                false, size(), stride
            )
            glEnableVertexAttribArray(index)
        }

        fun namedIndex(): Array<String> {
            return attributes.map { attribute -> attribute.name }.toTypedArray()
        }

        private fun size(): Int {
            return attributes.sumBy { attribute -> attribute.size } * floatSize // only upload floats
        }
    }
}
