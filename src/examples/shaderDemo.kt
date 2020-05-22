package examples

import engine.*
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture
import engine.math.Const
import engine.math.Copy
import engine.math.clamp
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL46

const val shaderName = "texture"

// possible shader names
// white
// striped
// wobble
// texture




fun main() {
    Game3().run()
}

class Game3 : Game() {
    init {
        Loader.root = "assets/examples"
    }

    private val loadedMesh = Loader.createViaPath(Mesh, "models/scene.fbx")
    private val tex = Loader.createViaPath(Texture, "textures/krakula-xl.png")
    private val shader: Shader = Loader.createViaPath(Shader, "shaders/demo/$shaderName.shader")
    private val logo = Texture.createViaPath("assets/engine/logo.png")

    private val world = Matrix4f()
    private val projection = Matrix4f()
    private val view = Matrix4f()

    private val position = Vector3f()
    private val rotation = Quaternionf()

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


        val move = Copy.zero
        val speed = 6f

        if (Input.Mouse.leftMouseButton) {
            Input.Mouse.setMode(GLFW.GLFW_CURSOR_DISABLED)
        }

        if (Input.Keyboard.down(GLFW.GLFW_KEY_W)) {
            move.add(Const.forward)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_S)) {
            move.add(Const.backwards)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_SPACE)) {
            move.add(Const.up)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            move.add(Const.down)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_A)) {
            move.add(Const.left)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_D)) {
            move.add(Const.right)
        }

        val sensitivity = .006f
        rotation.rotateAxis(Input.Mouse.deltaPosition.x * sensitivity, Const.up)
        rotation.rotateLocalX(Input.Mouse.deltaPosition.y * sensitivity)

        move.clamp(1f)
        move.mul(Time.deltaTime * speed)
        rotation.transformInverse(move)
        position.add(move)

        view.identity()
        view.rotation(rotation)
        view.translate(position)

        shader.setMatrix(world, view, projection)
        shader.setUniform("Time", Time.time)
        shader.setUniform("Texture", tex)

        shader.use {
            loadedMesh.draw()
        }

        super.draw()
    }
}
