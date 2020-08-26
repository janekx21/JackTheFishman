package dodgyDeliveries3.components

import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector2fConst
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times
import kotlin.math.sin

class WobbleProjectile : Projectile() {
    private var internalCollider: Collider? = null
    val collider: Collider
        get() {
            check(internalCollider != null)
            return internalCollider!!
        }

    override fun start() {
        super.start()
        internalCollider = gameObject.getComponent()
    }

    override fun update() {
        super.update()

        collider.velocity = collider.velocity + Vector2fConst.right * sin(Time.time * 10f) * .2f
    }
}
