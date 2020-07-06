import components.*
import engine.Game
import engine.Input
import engine.Loader
import engine.Time
import engine.Window
import engine.graphics.Mesh
import engine.math.times
import engine.graphics.Shader
import engine.graphics.Texture2D
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glCullFace
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.GL11C.GL_BACK
import org.lwjgl.opengl.GL11C.GL_CULL_FACE
import util.Debug

fun main() {
    DD3().run()
}

class DD3 : Game() {

    init {
        Loader.root = "DodgyDeliveries3/assets"
    }

    // private val diffuseShader = Loader.createViaPath(Shader, "shaders/default.shader")
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
        glCullFace(GL_BACK)
        glEnable(GL_CULL_FACE)
        // diffuseShader.setUniform("LightPositions[0]", Vector3f(0f, 0f, -8f))
        // diffuseShader.setUniform("LightColors[0]", Vector3f(1f, .6f, .5f) * 5f)

        // GameObject: Player
        GameObject("Player").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<ModelRenderer>().also {
                it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
                // it.shader = diffuseShader
            }
            gameObject.addComponent<CircleCollider>().also {
                it.velocity = Vector2fCopy.left * 1f
            }
            // TODO: add player controller when ready
            // gameObject.transform.scale.mul(0.1f)
            // gameObject.transform.position.x += 5f
            Scene.active.spawn(gameObject)
        }

        // GameObject: Player
        GameObject("Object").also { gameObject ->
            gameObject.addComponent<Transform>().also {
                it.position.set(Vector3f(-10f, 0f, .5f))
            }
            gameObject.addComponent<ModelRenderer>().also {
                it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
                // it.shader = diffuseShader
            }
            gameObject.addComponent<BoxCollider>().also {
                it.velocity = Vector2fCopy.right * 1f
            }
            // TODO: add player controller when ready
            // gameObject.transform.scale.mul(0.1f)
            // gameObject.transform.position.x += 5f
            Scene.active.spawn(gameObject)
        }


        // GameObject: Tunnel
        GameObject("Tunnel").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<ModelRenderer>().also {
                it.mesh = Loader.createViaPath(Mesh, "models/tunnel.fbx")
                // it.shader = diffuseShader
            }
            Scene.active.spawn(gameObject)
        }

        // GameObject: components.Camera
        GameObject("Camera").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<AudioListener>()
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

        GameObject("Light").also { gameObject ->
            gameObject.addComponent<Transform>()
            gameObject.addComponent<PointLight>().also {
                it.color = Vector3f(1f, .5f, .5f) * 2f
            }
            Scene.active.spawn(gameObject)
        }

        GameObject("Light").also { gameObject ->
            gameObject.addComponent<Transform>().apply {
                position.set(Vector3f(0f, 0f, 10f))
            }

            gameObject.addComponent<PointLight>().also {
                it.color = Vector3f(.5f, 1f, .5f) * 1f
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
        if (Input.Keyboard.down(GLFW_KEY_D)) {
            Scene.active.findViaName("components.Camera").transform.position.x += speed
        }
        if (Input.Keyboard.down(GLFW_KEY_A)) {
            Scene.active.findViaName("components.Camera").transform.position.x -= speed
        }
        if (Input.Keyboard.down(GLFW_KEY_S)) {
            Scene.active.findViaName("components.Camera").transform.position.z += speed
        }
        if (Input.Keyboard.down(GLFW_KEY_W)) {
            Scene.active.findViaName("components.Camera").transform.position.z -= speed
        }
        if (Input.Keyboard.down(GLFW_KEY_SPACE)) {
            Scene.active.findViaName("components.Camera").transform.position.y += speed
        }
        if (Input.Keyboard.down(GLFW_KEY_LEFT_CONTROL)) {
            Scene.active.findViaName("components.Camera").transform.position.y -= speed
        }

        // val player = Scene.active.findViaName("Player")
        // player.transform.position.set(player.getComponent<BoxCollider>().^)

        Scene.active.update()
    }

    override fun draw() {
        GL11.glClearColor(.5f, .5f, .5f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
        Debug.draw()
    }
}