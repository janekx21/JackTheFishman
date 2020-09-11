package jackTheFishman.examples


import jackTheFishman.framework.*
import jackTheFishman.framework.Loader.createViaPath
import jackTheFishman.framework.audio.OpenAlListener
import jackTheFishman.framework.graphics.CubeTexture
import jackTheFishman.framework.graphics.Mesh
import jackTheFishman.framework.graphics.Shader
import jackTheFishman.framework.graphics.Texture2D
import jackTheFishman.framework.math.Vector3fConst
import jackTheFishman.framework.math.clamp
import jackTheFishman.framework.math.plus
import jackTheFishman.framework.math.times
import org.joml.*
import org.lwjgl.opengl.GL46
import java.lang.Math

fun main() {
    ExampleFly().run()
}

class ExampleFly : Game() {
    init {
        Loader.rootPath = "jackTheFishman/examples"
    }

    private val loadedMesh = createViaPath<Mesh>("models/scene.fbx")
    private val tex = createViaPath<Texture2D>("textures/krakula-xl.png")
    private val shader: Shader = createViaPath("shaders/funky.shader")
    private val logo = createViaPath<Texture2D>("../engine/logo.png")
    private val cube = createViaPath<CubeTexture>("textures/cubeExample")

    private val world = Matrix4f()
    private val projection = Matrix4f()
    private val view = Matrix4f()

    private var position: Vector3fc = Vector3f()
    private var rotation = Quaternionf()

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


        var move: Vector3fc = Vector3fConst.zero
        val speed = 6f

        if (Input.Mouse.left.justDown) {
            Input.Mouse.setMode(Input.Mouse.CursorMode.DISABLED)
        }

        if (Input.Keyboard.down(Input.Keyboard.Keys.KEY_W)) {
            move += Vector3fConst.forward
        }
        if (Input.Keyboard.down(Input.Keyboard.Keys.KEY_S)) {
            move += Vector3fConst.backwards
        }
        if (Input.Keyboard.down(Input.Keyboard.Keys.KEY_SPACE)) {
            move += Vector3fConst.up
        }
        if (Input.Keyboard.down(Input.Keyboard.Keys.KEY_LEFT_SHIFT)) {
            move += Vector3fConst.down
        }
        if (Input.Keyboard.down(Input.Keyboard.Keys.KEY_A)) {
            move += Vector3fConst.left
        }
        if (Input.Keyboard.down(Input.Keyboard.Keys.KEY_D)) {
            move += Vector3fConst.right
        }

        val sensitivity = .006f
        rotation.rotateAxis(Input.Mouse.deltaPosition.x() * sensitivity, Vector3fConst.up)
        rotation.rotateLocalX(Input.Mouse.deltaPosition.y() * sensitivity)

        move.clamp(1f)
        move *= Time.deltaTime * speed
        move = with(Vector3f(move)) {
            rotation.transformInverse(this)
        }
        position += move

        view.identity()
        view.rotation(rotation)
        view.translate(Vector3f(position).negate())

        shader.setMatrix(world, view, projection, 0f)
        shader.setUniform("funkyColor", Vector4f(.2f, .5f, .7f, 1f))
        shader.setUniform("Cube", cube)
        shader.setUniform("CameraPosition", position)
        shader.setUniform("funkyTex", tex)
        shader.setUniform("LightDirection", Vector3f(.3f, -1f, .3f).normalize())

        shader.use {
            loadedMesh.draw()
        }
        OpenAlListener.position = position
        OpenAlListener.rotation = rotation

        super.draw()
    }
}
