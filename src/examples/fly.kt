package examples

import engine.*
import engine.graphics.CubeTexture
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture2D
import engine.math.Vector3fConst
import engine.math.Vector3fCopy
import engine.math.clamp
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

    private val loadedMesh = Loader.createViaPath(Mesh, "models/scene.fbx")
    private val tex = Loader.createViaPath(Texture2D, "textures/krakula-xl.png")
    private val shader: Shader = Loader.createViaPath(Shader, "shaders/funky.shader")
    private val logo = Texture2D.createViaPath("assets/engine/logo.png")
    private val cube = Loader.createViaPath(CubeTexture, "textures/cubeExample")

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
    }


    override fun draw() {
        GL46.glClearColor(.2f, .2f, .2f, 1f)
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT or GL46.GL_DEPTH_BUFFER_BIT)


        val move = Vector3fCopy.zero
        val speed = 6f

        if (Input.Mouse.left.justDown) {
            Input.Mouse.setMode(Input.Mouse.CursorMode.DISABLED)
        }

        if (Input.Keyboard.down(GLFW.GLFW_KEY_W)) {
            move.add(Vector3fConst.forward)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_S)) {
            move.add(Vector3fConst.backwards)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_SPACE)) {
            move.add(Vector3fConst.up)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            move.add(Vector3fConst.down)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_A)) {
            move.add(Vector3fConst.left)
        }
        if (Input.Keyboard.down(GLFW.GLFW_KEY_D)) {
            move.add(Vector3fConst.right)
        }

        val sensitivity = .006f
        rotation.rotateAxis(Input.Mouse.deltaPosition.x * sensitivity, Vector3fConst.up)
        rotation.rotateLocalX(Input.Mouse.deltaPosition.y * sensitivity)

        move.clamp(1f)
        move.mul(Time.deltaTime * speed)
        rotation.transformInverse(move)
        position.add(move)

        view.identity()
        view.rotation(rotation)
        view.translate(Vector3f(position).negate())

        shader.setMatrix(world, view, projection)
        shader.setUniform("funkyColor", Vector4f(.2f, .5f, .7f, 1f))
        shader.setUniform("Cube", cube)
        shader.setUniform("CameraPosition", position)
        shader.setUniform("funkyTex", tex)
        shader.setUniform("LightDirection", Vector3f(.3f, -1f, .3f).normalize())

        shader.use {
            loadedMesh.draw()
        }
        Audio.Listener.position = position
        Audio.Listener.rotation = rotation

        super.draw()
    }
}
