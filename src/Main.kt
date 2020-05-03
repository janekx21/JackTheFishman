import engine.Game
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL46
import kotlin.math.sin

fun main() {
    Game1().run()
}

class Game1 : Game() {

    private val loadedMesh = Mesh.createViePath("assets/models/arrow.obj")
    val tex = Texture.createViaPath("assets/textures/krakula-xl.png")
    private val shader: Shader = Shader("assets/shaders/vertex.glsl", "assets/shaders/fragment.glsl")

    override fun draw() {
        GL46.glClearColor(.2f, .2f, .2f, 1f)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)

        val world = Matrix4f()
        val projection = Matrix4f()
        val view = Matrix4f()

        projection.perspective(Math.toRadians(100.0).toFloat(), 1f, .1f, 10f)
        view.translate(Vector3f(.1f, -.4f, -.6f))
        world.translate(Vector3f(sin(GLFW.glfwGetTime()).toFloat() * .1f, 0f, 0f))

        shader.setMatrix(world, view, projection)
        shader.use {
            loadedMesh.draw()
        }

        super.draw()
    }
}
