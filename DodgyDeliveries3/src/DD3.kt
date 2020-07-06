
import components.*
import engine.*
import engine.graphics.Mesh
import engine.graphics.Texture2D
import engine.math.Vector3fConst
import engine.math.Vector3fCopy
import engine.math.plusAssign
import engine.math.times
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glCullFace
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL11C.GL_BACK
import org.lwjgl.opengl.GL11C.GL_CULL_FACE
import util.ColorPalette
import util.Debug

fun main() {
    DD3().run()
}

class DD3 : Game() {

    init {
        Loader.root = "DodgyDeliveries3/assets"
    }

    private val logo = Texture2D.createViaPath("assets/engine/logo.png")

    /*
    Camera Control:
    up: space
    down: left control
    left: a
    right: d
    forward: w
    backward: s

    press shift for speed boost
     */


    init {
        // set default texture color to white
        Texture2D.setDefaultTextureWhite()

        glCullFace(GL_BACK)
        glEnable(GL_CULL_FACE)

        // GameObject: Player
        GameObject("Player").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
            }
            gameObject.addComponent<CircleCollider>().apply {
                velocity = Vector2fCopy.left * 1f
            }
            // TODO: add player controller when ready
            Scene.active.spawn(gameObject)
        }

        // GameObject: Player
        GameObject("Object").also { gameObject ->
            gameObject.addComponent<Transform>().apply {
                position.set(Vector3f(-10f, 0f, .5f))
            }
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
            }
            gameObject.addComponent<BoxCollider>().apply {
                velocity = Vector2fCopy.right * 1f
            }
            // TODO: add player controller when ready
            Scene.active.spawn(gameObject)
        }


        // GameObject: Tunnel
        GameObject("Tunnel").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath(Mesh, "models/tunnel.fbx")
            }
            Scene.active.spawn(gameObject)
        }

        // GameObject: components.Camera
        GameObject("Camera").also { gameObject ->
            gameObject.addComponent<Transform>().apply {
                position.z = .5f
            }
            gameObject.addComponent<AudioListener>()
            Camera.main = gameObject.addComponent()
            Scene.active.spawn(gameObject)
            // TODO: add audio listener component
        }

        Window.onResize = {
            Camera.main?.getProjectionMatrix()?.identity()
            Camera.main?.getProjectionMatrix()?.perspective(Math.toRadians(90.0).toFloat(), it.aspect, .1f, 100f)
        }
        Window.onResize(Window)
        Window.setIcon(logo)

        GameObject("Light").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<PointLight>().apply {
                color = Vector3f(ColorPalette.BLUE) * 2f
            }
            Scene.active.spawn(gameObject)
        }

        GameObject("Light").also { gameObject ->
            gameObject.addComponent<Transform>().apply {
                position.set(Vector3f(2f, 0f, 10f))
            }

            gameObject.addComponent<PointLight>().apply {
                color = Vector3f(ColorPalette.ORANGE) * 1f
            }
            Scene.active.spawn(gameObject)
        }
    }

    override fun update() {
        // a movable camera is just for testing
        var speed = Time.deltaTime * 10f
        if (Input.Keyboard.down(GLFW_KEY_LEFT_SHIFT)) {
            speed *= 5f
        }
        val keyToDirection = mapOf(
            GLFW_KEY_D to Vector3fConst.right,
            GLFW_KEY_A to Vector3fConst.left,
            GLFW_KEY_W to Vector3fConst.forward,
            GLFW_KEY_S to Vector3fConst.backwards,
            GLFW_KEY_SPACE to Vector3fConst.up,
            GLFW_KEY_LEFT_CONTROL to Vector3fConst.down
        )

        val move = Vector3fCopy.zero
        for ((key, direction) in keyToDirection) {
            if (Input.Keyboard.down(key)) {
                move += Vector3f(direction)
            }
        }
        if (move.lengthSquared() > 0) {
            move.normalize()
        }
        Camera.main!!.transform.position += move * speed

        Scene.active.update()
    }

    override fun draw() {
        GL11.glClearColor(.1f, .1f, .15f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
        Debug.draw()
    }
}