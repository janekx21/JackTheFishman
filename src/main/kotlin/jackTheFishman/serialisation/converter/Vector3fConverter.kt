package jackTheFishman.serialisation.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import org.joml.Vector3f
import org.joml.Vector3fc

object Vector3fConverter : Converter {
    private const val keyForX = "x"
    private const val keyForY = "y"
    private const val keyForZ = "z"

    override fun canConvert(cls: Class<*>) = cls == Vector3f::class.java || cls == Vector3fc::class.java

    override fun toJson(value: Any): String {
        (value as Vector3fc).run {
            return """{$keyForX: ${x()}, $keyForY: ${y()}, $keyForZ: ${z()}}"""
        }
    }

    override fun fromJson(jv: JsonValue): Vector3fc {
        val vectorObject = jv.obj
        checkNotNull(vectorObject)
        vectorObject.also {
            val x = anyAsFloat(it[keyForX])
            val y = anyAsFloat(it[keyForY])
            val z = anyAsFloat(it[keyForZ])
            return Vector3f(x, y, z)
        }
    }
}
