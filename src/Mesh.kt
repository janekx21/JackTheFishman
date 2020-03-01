data class Mesh(public var vertexes: List<Vector>) {
    val size: Int
        get() = vertexes.size

    fun toFloatArray(): FloatArray {
        return FloatArray(vertexes.size * 2) { i ->
            if (i % 2 == 0) vertexes[i / 2].x else vertexes[i / 2].y
        }
    }

    fun toVertexBuffer(): VertexBuffer {
        return VertexBuffer(this)
    }

    operator fun plus(other: Mesh): Mesh {
        return Mesh(vertexes + other.vertexes);
    }

    public fun translate(delta: Vector): Mesh {
        return Mesh(vertexes.map { vector -> vector + delta })
    }
}
