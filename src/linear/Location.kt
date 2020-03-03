package linear

data class Location(var position: Vector, var rotation: Float, var scale: Vector) {
    companion object {
        val identity = Location(
            Vector.zero,
            0f,
            Vector.one
        )
    }
}