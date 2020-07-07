package components

import Component
import GameObject
import engine.math.Vector3fExt
import engine.math.toJson
import org.joml.Vector3f
import org.joml.Vector3fc

class PointLight(gameObject: GameObject) : Component(gameObject) {

    var color: Vector3fc = Vector3f(0f, 0f, 0f)

    override fun update() {
    }

    override fun draw() {
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