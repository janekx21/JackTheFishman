import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL46.*


fun initQuad() {
    var buffer = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, buffer)
    glBufferData(GL_ARRAY_BUFFER, floatArrayOf(0f,0f,0f,1f,1f,1f,0f,0f,1f,0f,1f,1f), GL_STATIC_DRAW)
    var positionAttr = glGetAttribLocation(0, "position")
    glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(positionAttr)
}

class Quad(var buffer: Int): IDrawable {


    override fun draw() {

    }
}