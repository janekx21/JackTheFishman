package linear

data class Rectangle(var position: Vector, var size: Vector) {
    var center: Vector
        get() = position
        set(value) {
            position = value
        }
}