package graphics

import linear.Vector

data class Mesh(public var vertexes: List<Vertex>) {
    val size: Int
        get() = vertexes.size

    fun toFloatArray(): FloatArray {
        var ret = floatArrayOf()
        vertexes.forEach { t: Vertex -> ret += t.toArray().toFloatArray() }
        return ret
    }

    fun toVertexBuffer(): VertexBuffer {
        return VertexBuffer(this)
    }

    operator fun plus(other: Mesh): Mesh {
        return Mesh(vertexes + other.vertexes);
    }
}
