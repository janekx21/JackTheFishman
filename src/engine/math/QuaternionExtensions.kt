package engine.math

import org.joml.Quaternionf

fun Quaternionf.toJson(): Any? {
    return arrayOf(this.x, this.y, this.z, this.w)
}

fun Quaternionf.fromJson(json: Any?) {
    val list = json as List<*>
    this.set(
        (list[0] as Double).toFloat(),
        (list[1] as Double).toFloat(),
        (list[2] as Double).toFloat(),
        (list[3] as Double).toFloat()
    )
}