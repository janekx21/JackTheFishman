package jackTheFishman.framework

import com.beust.klaxon.Klaxon
import jackTheFishman.framework.math.converter.QuaternionConverter
import jackTheFishman.framework.math.converter.Vector2fConverter
import jackTheFishman.framework.math.converter.Vector3fConverter

object Serialisation {
    val klaxon = Klaxon().run {
        converter(Vector2fConverter)
        converter(Vector3fConverter)
        converter(QuaternionConverter)
    }
}
