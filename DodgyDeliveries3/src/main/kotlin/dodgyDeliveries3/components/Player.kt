package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Input
import jackTheFishman.engine.Window
import jackTheFishman.engine.math.Vector2fConst
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times
import org.jbox2d.common.MathUtils.clamp

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
    }

    private fun handleInput() {
        targetPosition = clamp(mapScreenToView(Input.Mouse.position.x()), -1f, 1f) * laneWidth * .5f
    }

    private fun mapScreenToView(x: Float): Float {
        return (x / Window.size.x()) * 2f - 1f
    }

    private fun applyVelocityChange() {
        val delta = targetPosition - transform.position.x()
        val moddedDelta = delta - collider.velocity.x() * .05f
        val deltaClamped = clamp(moddedDelta, -maxVelocityChange, maxVelocityChange)
        collider.velocity += Vector2fConst.right * deltaClamped * speed
    }

    override fun draw() {}

    companion object {
        const val maxVelocityChange = 4f
        const val laneWidth = 8f
    }
}
