import engine.Game
import engine.Input
import engine.Loader
import engine.Window
import engine.graphics.Mesh
import engine.graphics.Shader
import engine.graphics.Texture2D
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11

fun main() {
    DD3().run()
}

class DD3 : Game() {

    init {
        Loader.root = "DodgyDeliveries3/assets"
    }

    private val diffuseshader = Loader.createViaPath(Shader, "shaders/diffuse.shader")
    private val logo = Texture2D.createViaPath("assets/engine/logo.png")


    /*
    CAMERA CONTROL:
    UP: SPACE
    DOWN: LEFT CONTROL
    LEFT: A
    RIGHT: D
    FORWARD: W
    BACKWARD: S

    PRESS SHIFT FOR SPEED BOOST
     */


    init {
        diffuseshader.setUniform("LightDirection", Vector3f(-1f, -1f, -1f))

        // GameObject: Player
        GameObject("Player").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<ModelRenderer>().also {
                it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
                it.shader = diffuseshader
            }
            // TODO: add player controller when ready
            gameObject.transform.scale.mul(0.1f)
            gameObject.transform.position.x += 5f
            Scene.active.spawn(gameObject)
        }


        // GameObject: Tunnel
        GameObject("Tunnel").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<ModelRenderer>().also {
                it.mesh = Loader.createViaPath(Mesh, "models/tunnel.fbx")
                it.shader = diffuseshader
            }
            Scene.active.spawn(gameObject)
        }

        // GameObject: Camera
        GameObject("Camera").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.transform.position.z = 0.5f
            Camera.main = gameObject.addComponent()
            Scene.active.spawn(gameObject)
            // TODO: add audio listener component
        }

        Window.onResize = {
            Camera.main?.getProjectionMatrix()?.identity()
            Camera.main?.getProjectionMatrix()?.perspective(Math.toRadians(80.0).toFloat(), it.aspect, .1f, 10f)
        }
        Window.onResize(Window)
        Window.setIcon(logo)
    }

    override fun update() {
        // a movable camera is just for testing
        var speed = 0.001f
        if (Input.Keyboard.down(GLFW_KEY_LEFT_SHIFT)) {
            speed = 0.01f
        }
        if (Input.Keyboard.down(GLFW_KEY_D)) {
            Scene.active.findViaName("Camera").transform.position.x += speed
        }
        if (Input.Keyboard.down(GLFW_KEY_A)) {
            Scene.active.findViaName("Camera").transform.position.x -= speed
        }
        if (Input.Keyboard.down(GLFW_KEY_S)) {
            Scene.active.findViaName("Camera").transform.position.z += speed
        }
        if (Input.Keyboard.down(GLFW_KEY_W)) {
            Scene.active.findViaName("Camera").transform.position.z -= speed
        }
        if (Input.Keyboard.down(GLFW_KEY_SPACE)) {
            Scene.active.findViaName("Camera").transform.position.y += speed
        }
        if (Input.Keyboard.down(GLFW_KEY_LEFT_CONTROL)) {
            Scene.active.findViaName("Camera").transform.position.y -= speed
        }
    }

    override fun draw() {
        GL11.glClearColor(.5f, .5f, .5f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
    }
}