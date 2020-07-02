package engine.math

import engine.util.IJsonUnserializable
import org.joml.Quaternionf

object QuaternionfExt : IJsonUnserializable<Quaternionf> {
    override fun fromJson(json: Any?): Quaternionf {
        val list = json as List<*>

        return Quaternionf(
            (list[0] as Double).toFloat(),
            (list[1] as Double).toFloat(),
            (list[2] as Double).toFloat(),
            (list[3] as Double).toFloat()
        )
    }
}

fun Quaternionf.toJson(): Any? {
    return arrayOf(this.x, this.y, this.z, this.w)
}

