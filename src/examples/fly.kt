package examples

import engine.*
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture
import engine.util.*
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL46

fun main() {
    Game2().run()
}

class Game2 : Game() {
    init {
        Loader.root = "assets/examples"
    }

    private val loadedMesh = Loader.createViaPath(Mesh, "models/street.fbx")
    private val tex = Loader.createViaPath(Texture, "textures/krakula-xl.png")
    private val shader: Shader = Loader.createViaPath(Shader, "shaders/funky.shader")

    private val world = Matrix4f()
    private val projection = Matrix4f()
    private val view = Matrix4f()

    private val position = Vector3f()
    private val rotation = Quaternionf()

    init {
        Window.onResize = {
            projection.identity()
            projection.perspective(Math.toRadians(100.0).toFloat(), it.aspect, .1f, 10f)
        }
        Window.onResize(Window)

        world.translate(0f, 0f, -3f)
        Input.Mouse.setMode(GLFW.GLFW_CURSOR_DISABLED)
    }


    override fun draw() {
        GL46.glClearColor(.2f, .2f, .2f, 1f)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)


        val move = zero
        val speed = 8f

        if (Input.Keyboard.down(GLFW.GLFW_KEY_W)) {
            move.add(forward)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_S)) {
            move.add(backwards)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_SPACE)) {
            move.add(up)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            move.add(down)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_A)) {
            move.add(left)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_D)) {
            move.add(right)
        }

        val sensitivity = .006f
        rotation.rotateAxis(Input.Mouse.deltaPosition.x * sensitivity, up)
        rotation.rotateLocalX(Input.Mouse.deltaPosition.y * sensitivity)

        move.clamp(1f)
        move.mul(Time.deltaTime * speed)
        rotation.transformInverse(move)
        position.add(move)

        view.identity()
        view.rotation(rotation)
        view.translate(position)

        shader.setMatrix(world, view, projection)
        shader.setUniform("funkyColor", Vector4f(.2f, .5f, .7f, 1f))
        shader.setUniform("funkyTex", tex)

        shader.use {
            loadedMesh.draw()
        }

        super.draw()
    }
}
