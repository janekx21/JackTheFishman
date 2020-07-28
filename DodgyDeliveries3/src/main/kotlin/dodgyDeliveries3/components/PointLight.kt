package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.math.Vector3fExt
import jackTheFishman.engine.math.toJson
import org.joml.Vector3f
import org.joml.Vector3fc

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

    override fun toJson(): Any? {
        return mapOf(
            "color" to color.toJson()
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        color = Vector3fExt.fromJson(map["color"])
    }

    companion object {
        const val max = 32
        val all = arrayListOf<PointLight>()
    }
}