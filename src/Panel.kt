class Panel(var position: Vector, var size: Vector) : IDrawable, Component.Final(Vector.one) {

    private val quad = Model(primitiveQuadBuffer, DefaultShader, Location.zero, Texture("assets/ex.png"))

    override fun draw() {
        quad.setPosition(position - size * .5f)
        quad.setScale(size)
        quad.draw()
    }

    override fun draw(position: Vector, size: Vector) {
        this.position = position
        this.size = size
        draw()
    }
}