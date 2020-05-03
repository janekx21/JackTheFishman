import engine.Game
import engine.Input
import engine.Time
import engine.Window
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL46
import kotlin.math.sin

fun main() {
    Game1().run()
}

class Game1 : Game() {

    private val loadedMesh = Mesh.createViePath("assets/models/arrow.obj")
    private val tex = Texture.createViaPath("assets/textures/krakula-xl.png")
    private val shader: Shader = Shader("assets/shaders/vertex.glsl", "assets/shaders/funky.glsl")

    val world = Matrix4f()
    val projection = Matrix4f()
    val view = Matrix4f()

    init {
        Window.onResize = {
            projection.identity()
            projection.perspective(Math.toRadians(100.0).toFloat(), it.aspect, .1f, 10f)
        }
        Window.onResize(Window)
    }


    override fun draw() {
        GL46.glClearColor(.2f, .2f, .2f, 1f)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)

        view.identity()
        view.rotateZ(Time.time * .1f)
        view.translate(Vector3f(.1f, -.4f, -.6f))

        world.identity()
        world.translate(Vector3f(sin(GLFW.glfwGetTime()).toFloat() * .1f, 0f, 0f))

        shader.setMatrix(world, view, projection)
        shader.setUniform("funkyColor", Vector4f(.2f, .5f, .7f, 1f))
        shader.setUniform("funkyTex", tex)

        if (Input.Keyboard.down(GLFW.GLFW_KEY_SPACE)) {
            shader.setUniform("funkyColor", Vector4f(.9f, .2f, .2f, 1f))
        }
        shader.use {
            loadedMesh.draw()
        }

        super.draw()
    }
}
