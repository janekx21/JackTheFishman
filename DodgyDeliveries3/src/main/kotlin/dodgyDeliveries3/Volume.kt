package dodgyDeliveries3

import com.beust.klaxon.Klaxon

data class Volume(var volume: Float) {
    fun test() {
        Klaxon().parse<Volume>("""""")
    }
}
