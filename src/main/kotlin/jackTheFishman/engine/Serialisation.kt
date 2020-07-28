package jackTheFishman.engine

import com.beust.klaxon.Klaxon
import jackTheFishman.engine.math.converter.QuaternionConverter
import jackTheFishman.engine.math.converter.Vector2fConverter
import jackTheFishman.engine.math.converter.Vector3fConverter

object Serialisation {
    val klaxon = Klaxon().run {
        converter(Vector2fConverter)
        converter(Vector3fConverter)
        converter(QuaternionConverter)
    }
}