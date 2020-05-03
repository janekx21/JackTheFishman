import engine.Game
import engine.Time
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

    init {
        window.onResize = {
            with(it) {
                GL46.glViewport(0, 0, size.x, size.y)
                GL46.glLoadIdentity()
                GL46.glTranslatef(0f, 0f, -1f)
                GL46.glTranslatef(0f, -.5f, 0f)
                GL46.glMatrixMode(GL46.GL_PROJECTION)
                GL46.glLoadIdentity()
                GL46.glFrustum(-fov * aspect, fov * aspect, -fov, fov, .01, 10.0)
                GL46.glMatrixMode(GL46.GL_MODELVIEW)
            }
        }
    }

    override fun draw() {
        GL46.glClearColor(.2f, .2f, .2f, 1f)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)

        val world = Matrix4f()
        val projection = Matrix4f()
        val view = Matrix4f()

        projection.perspective(Math.toRadians(100.0).toFloat(), 1f, .1f, 10f)
        view.rotateZ(Time.time * .1f)
        view.translate(Vector3f(.1f, -.4f, -.6f))
        world.translate(Vector3f(sin(GLFW.glfwGetTime()).toFloat() * .1f, 0f, 0f))

        shader.setMatrix(world, view, projection)
        shader.use {
            loadedMesh.draw()
        }

        super.draw()
    }
}
