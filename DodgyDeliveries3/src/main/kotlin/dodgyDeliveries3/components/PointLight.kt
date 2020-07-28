package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.util.Debug
import org.joml.Vector3f
import org.joml.Vector3fc

data class PointLight(var color: Vector3fc = Vector3f(0f, 0f, 0f)) : Component() {

    override fun update() {
    }

    override fun draw() {
        if (Debug.active) {
            Debug.drawWiredSphere(transform.position, .1f, color)
        }
    }

    override fun onEnable() {
        check(all.size < max) { "you can only have $max PointLight's" }
        all.add(this)
    }

    override fun onDisable() {
        all.remove(this)
    }

    companion object {
        const val max = 32
        val all = arrayListOf<PointLight>()
    }
}