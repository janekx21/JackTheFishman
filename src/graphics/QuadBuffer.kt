package graphics

import linear.Vector

val primitiveQuad = Mesh(
    listOf(
        Vertex(
            Vector(0f, 0f),
            Vector(0f, 0f)
        ),
        Vertex(
            Vector(1f, 0f),
            Vector(1f, 0f)
        ),
        Vertex(
            Vector(1f, 1f),
            Vector(1f, 1f)
        ),
        Vertex(
            Vector(0f, 1f),
            Vector(0f, 1f)
        )
    )
)

object QuadBuffer : VertexBuffer(primitiveQuad) {}


