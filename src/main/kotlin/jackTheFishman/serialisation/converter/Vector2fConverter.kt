package jackTheFishman.serialisation.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import org.joml.Vector2f
import org.joml.Vector2fc

object Vector2fConverter : Converter {
    private const val keyForX = "x"
    private const val keyForY = "y"

    override fun canConvert(cls: Class<*>) = cls == Vector2f::class.java || cls == Vector2fc::class.java

    override fun toJson(value: Any): String {
        (value as Vector2fc).run {
            return """{$keyForX: ${x()}, $keyForY: ${y()}}"""
        }
    }

    override fun fromJson(jv: JsonValue): Vector2fc {
        val vectorObject = jv.obj
        checkNotNull(vectorObject)
        vectorObject.also {
            val x = anyAsFloat(it[keyForX])
            val y = anyAsFloat(it[keyForY])
            return Vector2f(x, y)
        }
    }
}
