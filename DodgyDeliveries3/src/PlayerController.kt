import components.Collider
import engine.Input
import org.joml.Vector2f
import kotlin.math.pow

class PlayerController(gameObject: GameObject) : Component(gameObject) {

    override fun update() {
        handleMovement()
    }

    override fun draw() {
    }

    private val speed = 1f
    private val bounceBack = 20f

    // provisionally only supports mouse -- TODO: add keyboard and controller
    private fun handleMovement() {
        val col = gameObject.getComponent<Collider>()
        col.velocity = Vector2f(col.velocity.x(), 0f)
        col.applyForceToCenter(Vector2f(Input.Mouse.previousPosition.x() * speed, 0f))

        if (transform.position.x() > 4f) {
            col.applyForceToCenter(
                Vector2f(
                    -((transform.position.x() - 4f) * bounceBack).toDouble().pow(2.0).toFloat(),
                    0f
                )
            )
        }
        if (transform.position.x() < -4f) {
            col.applyForceToCenter(
                Vector2f(
                    ((transform.position.x() + 4f) * bounceBack).toDouble().pow(2.0).toFloat(),
                    0f
                )
            )
        }
    }
}