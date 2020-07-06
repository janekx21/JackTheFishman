import components.Collider
import engine.Input
import org.joml.Vector2f

class PlayerController(gameObject: GameObject) : Component(gameObject) {

    override fun update() {
        controller()
        friction()
    }

    override fun draw() {
    }

    private val speed = 0.001f

    // provisionally only supports mouse -- TODO: add keyboard and controller
    fun controller() {
        val col = gameObject.getComponent<Collider>()
        col.velocity = Vector2f(col.velocity.x() + Input.Mouse.deltaPosition.x * speed, col.velocity.y())
    }

    // provisionally friction -- very bad code
    fun friction() {
        val col = gameObject.getComponent<Collider>()
        col.velocity = Vector2f(col.velocity.x() * 0.9f, col.velocity.y() * 0.9f)
        if (col.velocity.x() < 0.01f && col.velocity.x() > -0.01f) {
            col.velocity = Vector2f(0f, col.velocity.y())
        }
        if (col.velocity.y() < 0.01f && col.velocity.y() > -0.01f) {
            col.velocity = Vector2f(col.velocity.x(), 0f)
        }
    }

}