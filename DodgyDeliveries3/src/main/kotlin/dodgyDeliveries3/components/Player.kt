package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Input
import jackTheFishman.engine.Time
import jackTheFishman.engine.Window
import jackTheFishman.engine.math.Vector2fConst
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times
import org.jbox2d.common.MathUtils.PI
import org.jbox2d.common.MathUtils.clamp
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.sin

class Player(var speed: Float = 8f) : Component() {
    var internalcollider: Collider? = null
    private val collider: Collider
        get() {
            check(internalcollider != null) { "Collider not found" }
            return internalcollider!!
        }

    var targetPosition = 0f

    override fun start() {
        internalcollider = gameObject.getComponent()
        check(internalcollider != null) { "Player Component needs a Collider" }
    }

    override fun update() {
        handleInput()
        applyVelocityChange()
        animateRotation()
        animateYAxis()
        handleHealth()
    }

    private fun handleInput() {
        targetPosition = clamp(mapScreenToView(Input.Mouse.position.x()), -1f, 1f) * laneWidth * .5f
    }

    private fun mapScreenToView(x: Float): Float {
        return (x / Window.physicalSize.x()) * 2f - 1f
    }

    private fun applyVelocityChange() {
        val delta = targetPosition - transform.position.x()
        val moddedDelta = delta - collider.velocity.x() * .05f
        val deltaClamped = clamp(moddedDelta, -maxVelocityChange, maxVelocityChange)
        collider.velocity += Vector2fConst.right * deltaClamped * speed
    }

    private fun animateRotation() {
        val turnAmount = -collider.velocity.x() * .02f
        val clampedTurnAmount = clamp(turnAmount, -PI / 4, PI / 4) * Time.deltaTime
        transform.rotation = Quaternionf()
            .rotateAxis(clampedTurnAmount + PI, Vector3fConst.up)
            .rotateAxis(sin(Time.time * 3f) * .1f, Vector3fConst.right)
    }

    private fun animateYAxis() {
        val y = sin(Time.time * 3f) * .1f
        transform.position = Vector3f(transform.position.x(), y, transform.position.z())
    }

    private fun handleHealth() {
        val health = gameObject.getComponent<Health>()
        if (!health.alive) {
            // TODO handle game over
        }
    }

    companion object {
        const val maxVelocityChange = 4f
        const val laneWidth = 8f
    }
}
