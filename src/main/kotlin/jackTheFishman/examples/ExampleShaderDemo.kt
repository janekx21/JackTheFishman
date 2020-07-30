package jackTheFishman.examples

import jackTheFishman.engine.Game
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Loader.createViaPath
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Shader
import jackTheFishman.engine.graphics.Texture2D
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import org.lwjgl.opengl.GL46

/**
 * possible shader names:
 * white,
 * striped,
 * wobble,
 * texture,
 * demo
 */
const val shaderName = "demo"

fun main() {
    ExampleShaderDemo().run()
}

class ExampleShaderDemo : Game() {
    init {
        Loader.rootPath = "assets/examples"
    }

    private val loadedMesh = createViaPath<Mesh>("models/scene.fbx")
    private val tex = createViaPath<Texture2D>("textures/krakula-xl.png")
    private val shader: Shader = createViaPath("shaders/demo/$shaderName.shader")
    private val logo = Texture2D.createViaPath("assets/engine/logo.png")

    private val world = Matrix4f()
    private val projection = Matrix4f()
    private val view = Matrix4f()

    init {
        Window.onResize = {
            projection.identity()
            projection.perspective(Math.toRadians(80.0).toFloat(), it.aspect, .1f, 10f)
        }
        Window.onResize(Window)
        Window.setIcon(logo)

        world.translate(0f, 0f, -3f)
    }


    override fun draw() {
        GL46.glClearColor(.2f, .2f, .2f, 1f)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)

        view.identity()
        view.rotation(Quaternionf())
        view.translate(Vector3f())

        shader.setMatrix(world, view, projection)
        shader.setUniform("Time", Time.time)
        shader.setUniform("Texture", tex)

        shader.use {
            loadedMesh.draw()
        }

        super.draw()
    }
}
