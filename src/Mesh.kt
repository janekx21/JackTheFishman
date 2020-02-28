data class Mesh(public val vertexes: List<Vector>) {
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
}
