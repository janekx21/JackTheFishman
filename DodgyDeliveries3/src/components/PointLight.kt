package components

import Component
import GameObject
import org.joml.Vector3f
import org.joml.Vector3fc
import util.Debug

class PointLight(gameObject: GameObject) : Component(gameObject) {

    var color: Vector3fc = Vector3f(0f, 0f, 0f)

    override fun update() {
    }

    override fun draw() {
        if(Debug.active) {
            Debug.drawWiredSphere(transform.position, .1f, color)
        }
    }

    override fun onEnable() {
        check(all.size < max) {"you can only have $max PointLight's"}
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