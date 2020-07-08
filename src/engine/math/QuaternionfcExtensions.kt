package engine.math

import org.joml.Quaternionfc

fun Quaternionfc.toJson(): Any? {
    return arrayOf(this.x(), this.y(), this.z(), this.w())
}