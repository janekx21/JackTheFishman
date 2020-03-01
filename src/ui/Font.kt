package ui

import com.beust.klaxon.Klaxon
import graphics.Texture
import util.Path
import java.io.File

class Font(private val path: String) {
    data class Size(val width: Int, val height: Int) {}
    data class Info(val name: String, val size: Int, val rect: Size, val chars: Map<Int, Int>) {}

    val texture = Texture(Path.combine(path, "image.png"))

    private val info: Info

    init {
        val i = Klaxon().parse<Info>(File(Path.combine(path, "info.json")))
        check(i != null) { "font info could not be loadet" }
        info = i
    }
}