import engine.Input
import engine.math.Vector3fCopy
import engine.math.clamp
import org.lwjgl.glfw.GLFW.*

class PlayerController(gameObject: GameObject) : Component(gameObject) {
    override fun update() {
        controller()
    }

    override fun draw() {
    }

    var speed = 1f

    fun controller() {
        var transform = gameObject.getComponent<Transform>()
        val change = Vector3fCopy.zero
        change.add(Input.Mouse.deltaPosition.x * speed, Input.Mouse.deltaPosition.y * speed, 0f)
        change.add(Input.Controller.leftStick.x * speed, Input.Controller.leftStick.y * speed, 0f)
        if (Input.Keyboard.down(GLFW_KEY_W))
            change.add(0f, speed, 0f)
        if (Input.Keyboard.down(GLFW_KEY_S))
            change.add(0f, -speed, 0f)
        if (Input.Keyboard.down(GLFW_KEY_A))
            change.add(-speed, 0f, 0f)
        if (Input.Keyboard.down(GLFW_KEY_D))
            change.add(speed, 0f, 0f)
        change.clamp(1f) // Clampvalue
        transform.position.add(change)
    }

}