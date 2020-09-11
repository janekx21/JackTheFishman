package jackTheFishman.framework.math.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import jackTheFishman.framework.util.anyAsFloat
import org.joml.Vector3f
import org.joml.Vector3fc

object Vector3fConverter : Converter {
    override fun canConvert(cls: Class<*>) = cls == Vector3f::class.java || cls == Vector3fc::class.java

    override fun toJson(value: Any): String {
        (value as Vector3fc).run {
            return """{"x": ${x()}, "y": ${y()}, "z": ${z()}}"""
        }
    }

    override fun fromJson(jv: JsonValue): Vector3fc {
        check(jv.obj != null)
        jv.obj!!.also {
            val x = anyAsFloat(it["x"])
            val y = anyAsFloat(it["y"])
            val z = anyAsFloat(it["z"])
            return Vector3f(x, y, z)
        }
    }
}
