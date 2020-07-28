package jackTheFishman.engine.math.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import jackTheFishman.engine.util.anyAsFloat
import org.joml.Vector2f
import org.joml.Vector2fc

object Vector2fConverter : Converter {
    override fun canConvert(cls: Class<*>) = cls == Vector2f::class.java || cls == Vector2fc::class.java

    override fun toJson(value: Any): String {
        (value as Vector2fc).run {
            return """{"x": ${x()}, "y": ${y()}}"""
        }
    }

    override fun fromJson(jv: JsonValue): Vector2fc {
        check(jv.obj != null)
        jv.obj!!.also {
            val x = anyAsFloat(it["x"])
            val y = anyAsFloat(it["y"])
            return Vector2f(x, y)
        }
    }
}
