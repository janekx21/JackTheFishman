package ui

import graphics.*
import linear.Location
import linear.Vector

class Text(val font: Font, var text: String) : IDrawable {
    var model: Model? = null

    init {
        generate()
    }

    private fun generate() {
        val vertex = mutableListOf<Vertex>()
        var x = 0f
        for (char in text) {
            val pos = font.info.chars.find { c -> c.char == char.toInt() }?.pos
            check(pos != null) { "char not found" }
            val width = 1f / font.info.rect.width
            val relPos = pos.toFloat() / (font.info.rect.width.toFloat() * font.info.chars.size)
            vertex += listOf(
                Vertex(Vector(x, 0f), Vector(relPos, 0f)),
                Vertex(Vector(x + 1f, 0f), Vector(relPos + width, 0f)),
                Vertex(Vector(x + 1f, 1f), Vector(relPos + width, 1f)),
                Vertex(Vector(x, 1f), Vector(relPos, 1f))
            )
            x += 1f
            break
        }
        model = Model(
            Mesh(vertex).toVertexBuffer(),
            Shader("assets/shaders/fragment.glsl", "assets/shaders/vertex.glsl"),
            Location(Vector(0f,0f), 0f, Vector(1f,1f)),
            font.texture
        )
        model?.setScale(Vector(.1f, 1f))
        // model = Model(Mesh(vertex).toVertexBuffer(), DefaultShader, Location.identity, font.texture)
        println("model size: ${vertex.size}")
    }

    override fun draw() {
        model?.draw()
    }
}