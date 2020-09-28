package jackTheFishman.serialisation.converter

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import org.joml.Quaternionf
import org.joml.Quaternionfc

object QuaternionConverter : Converter {
    private const val keyForX = "x"
    private const val keyForY = "y"
    private const val keyForZ = "z"
    private const val keyForW = "w"

    override fun canConvert(cls: Class<*>) = cls == Quaternionf::class.java || cls == Quaternionfc::class.java

    override fun toJson(value: Any): String {
        (value as Quaternionfc).run {
            return """{$keyForX: ${x()}, $keyForY: ${y()}, $keyForZ: ${z()}, $keyForW: ${w()}}"""
        }
    }

    override fun fromJson(jv: JsonValue): Quaternionfc {
        val quaternionObject = jv.obj
        checkNotNull(quaternionObject)
        quaternionObject.also {
            val x = anyAsFloat(it[keyForX])
            val y = anyAsFloat(it[keyForY])
            val z = anyAsFloat(it[keyForZ])
            val w = anyAsFloat(it[keyForW])
            return Quaternionf(x, y, z, w)
        }
    }
}
