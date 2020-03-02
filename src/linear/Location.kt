package linear

data class Location(var position: Vector, var rotaion: Float, var scale: Vector) {
    companion object {
        val zero = Location(
            Vector.zero,
            0f,
            Vector.zero
        )
    }
}