import engine.Game
import engine.Input
import engine.Loader
import engine.graphics.Mesh
import engine.graphics.Shader
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
    private val cam = GameObject("Camera")
    private val player = GameObject("Player") // TODO: Change to playerclass when ready

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
        player.addComponent<Transform>()
        player.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx") // TODO: add player mesh
            it.shader = diffuseshader
        }
        // TODO: add player controller when ready
        player.transform.scale.mul(0.1f)
        player.transform.position.x += 5f
        Scene.active.spawn(player)

        // GameObject: Tunnel
        val tunnel = GameObject("Tunnel")
        tunnel.addComponent<Transform>()
        tunnel.addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath(Mesh, "models/cube.fbx") // TODO: add tunnel mesh
            it.shader = diffuseshader
        }
        tunnel.transform.scale.mul(0.1f)
        tunnel.transform.position.x -= 5f
        Scene.active.spawn(tunnel)

        // GameObject: Camera
        cam.addComponent<Transform>()
        Camera.main = cam.addComponent()
        // TODO: add audio component
        cam.transform.position.z = 0.5f
        Scene.active.spawn(cam)

    }

    override fun update() {
        var speed = 0.001f
        if (Input.Keyboard.down(GLFW_KEY_LEFT_SHIFT))
            speed = 0.01f

        if (Input.Keyboard.down(GLFW_KEY_D))
            cam.transform.position.x += speed
        if (Input.Keyboard.down(GLFW_KEY_A))
            cam.transform.position.x -= speed
        if (Input.Keyboard.down(GLFW_KEY_S))
            cam.transform.position.z += speed
        if (Input.Keyboard.down(GLFW_KEY_W))
            cam.transform.position.z -= speed
        if (Input.Keyboard.down(GLFW_KEY_SPACE))
            cam.transform.position.y += speed
        if (Input.Keyboard.down(GLFW_KEY_LEFT_CONTROL))
            cam.transform.position.y -= speed

    }

    override fun draw() {
        GL11.glClearColor(.5f, .5f, .5f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        Scene.active.draw()
    }
}