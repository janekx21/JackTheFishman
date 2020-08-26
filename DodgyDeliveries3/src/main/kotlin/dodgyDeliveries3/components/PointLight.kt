package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.moveTowards
import org.joml.Vector3f
import org.joml.Vector3fc

data class PointLight(var color: Vector3fc = Vector3f(0f, 0f, 0f)) : Component() {

    var animatedColor: Vector3fc = Vector3fConst.zero
    var targetColor: Vector3fc = Vector3fConst.zero
    var alive = true

    override fun start() {
        check(all.size < max) { "you can only have $max PointLight's" }
        all.add(this)
    }

    override fun update() {
        targetColor = (if (alive) color else Vector3fConst.zero)
        animatedColor = animatedColor.moveTowards(targetColor, Time.deltaTime * animationSpeed)
    }

    override fun draw() {
        if (Debug.active) {
            Debug.drawWiredSphere(transform.position, .1f, color)
        }
    }

    override fun stop() {
        alive = false
    }

    companion object {
        const val max = 32
        val all = arrayListOf<PointLight>()
        const val animationSpeed = 3f
    }
}
