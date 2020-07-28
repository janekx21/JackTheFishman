package jackTheFishman.engine.math

import jackTheFishman.engine.util.IJsonUnserializable
import org.joml.Matrix4f

object Matrix4fExt : IJsonUnserializable<Matrix4f> {
    override fun fromJson(json: Any?): Matrix4f {
        val array = json as List<*>

        val result = Matrix4f()
        (0 .. 15).forEach {
            result[it % 4, it / 4] = (array[it] as Double).toFloat()
        }

        return result
    }
}

fun Matrix4f.toJson(): Any? {
    return arrayOf(
        this[0, 0], this[1, 0], this[2, 0], this[3, 0],
        this[0, 1], this[1, 1], this[2, 1], this[3, 1],
        this[0, 2], this[1, 2], this[2, 2], this[3, 2],
        this[0, 3], this[1, 3], this[2, 3], this[3, 3]
    )
}

