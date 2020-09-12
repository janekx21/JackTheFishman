package jackTheFishman.framework

import com.beust.klaxon.Klaxon
import jackTheFishman.framework.math.converter.QuaternionConverter
import jackTheFishman.framework.math.converter.Vector2fConverter
import jackTheFishman.framework.math.converter.Vector3fConverter

object JsonSerialisation {
    val klaxon = Klaxon().run {
        converter(Vector2fConverter)
        converter(Vector3fConverter)
        converter(QuaternionConverter)
    }

    fun json(obj: Any): String {
        return klaxon.toJsonString(obj)
    }

    inline fun <reified T> parse(json: String): T where T : Any {
        val result = klaxon.parse<T>(json)
        checkNotNull(result) { "Deserialization failed" }
        return result
    }
}
