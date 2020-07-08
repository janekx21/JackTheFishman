package jackTheFishman.engine

import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser

object Json {
    fun encode(json: Any?): String {
        return Klaxon().toJsonString(json)
    }

    fun decode(jsonStr: String): Any? {
        return Parser.default().parse(StringBuilder(jsonStr))
    }
}