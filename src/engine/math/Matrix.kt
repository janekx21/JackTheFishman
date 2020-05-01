package engine.math

data class Matrix(private val data: FloatArray) {
    companion object {
        const val dim = 4
        const val size = dim * dim

        val identity =
            Matrix(FloatArray(size) { i -> if (i % (dim + 1) == 0) 1f else 0f })
    }

    init {
        assert(data.size == size)
    }

    fun index(pos: Point): Int {
        return pos.x + pos.y * dim
    }

    fun index(x: Int, y: Int): Int {
        return x + y * dim
    }

    private fun index(i: Int): Point {
        return Point(
            i % dim,
            i / dim
        )
    }

    operator fun times(other: Matrix): Matrix {
        return Matrix(FloatArray(size) { i ->
            val pos = index(i)
            FloatArray(dim) { j ->
                this.data[index(j, pos.y)] * other.data[index(pos.x, j)]
            }.sum()
        })
    }

    override fun toString(): String {
        return "[${data.joinToString(separator = ",")}]"
    }
}