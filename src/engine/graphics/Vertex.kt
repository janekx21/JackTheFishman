package engine.graphics

import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector3fc

import org.lwjgl.opengl.GL46
import org.lwjgl.opengl.GL46.GL_FLOAT

data class Vertex(val position: Vector3f, val uv: Vector2f, val normal: Vector3f, val tangent: Vector3fc) {

    data class Attribute(val name: String, val size: Int, val type: Int)

    fun toBuffer(): List<Float> {
        return listOf(
            position.x,
            position.y,
            position.z,
            uv.x,
            uv.y,
            normal.x,
            normal.y,
            normal.z,
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
                    GL46.glVertexAttribPointer(
                        this, attribute.value.size, attribute.value.type,
                        false, size(), stride.toLong()
                    )
                    GL46.glEnableVertexAttribArray(this)
                    stride += attribute.value.size * floatSize // only upload floats
                }
            }
        }

        fun namedIndex(): Array<String> {
            return attributes.map { attribute -> attribute.name }.toTypedArray()
        }

        private fun size(): Int {
            return attributes.sumBy { attribute -> attribute.size } * floatSize // only upload floats
        }
    }
}