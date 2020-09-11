package jackTheFishman.framework.math.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import jackTheFishman.framework.util.anyAsFloat
import org.joml.Quaternionf
import org.joml.Quaternionfc

object QuaternionConverter : Converter {
    override fun canConvert(cls: Class<*>) = cls == Quaternionf::class.java || cls == Quaternionfc::class.java

    override fun toJson(value: Any): String {
        (value as Quaternionfc).run {
            return """{"x": ${x()}, "y": ${y()}, "z": ${z()}, "w": ${w()}}"""
        }
    }

    override fun fromJson(jv: JsonValue): Quaternionfc {
        check(jv.obj != null)
        jv.obj!!.also {
            val x = anyAsFloat(it["x"])
            val y = anyAsFloat(it["y"])
            val z = anyAsFloat(it["z"])
            val w = anyAsFloat(it["w"])
            return Quaternionf(x, y, z, w)
        }
    }
}
