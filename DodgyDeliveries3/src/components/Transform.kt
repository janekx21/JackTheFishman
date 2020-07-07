package components

import Component
import GameObject
import engine.math.QuaternionfExt
import engine.math.Vector3fExt
import engine.math.toJson
import engine.math.Vector3fConst
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Quaternionfc
import org.joml.Vector3fc

class Transform(gameObject: GameObject) : Component(gameObject) {
    var position: Vector3fc = Vector3fConst.zero
    var rotation: Quaternionfc = Quaternionf()
    var scale: Vector3fc = Vector3fConst.one
    var parent: Transform? = null

    private var hash = 0
    private val cached = Matrix4f()

    override fun update() {}
    override fun draw() {}

    fun generateMatrix(): Matrix4f {
        if (hash == getMatrixHash()) {
            return cached
        }
        val matrix = Matrix4f().scale(scale).rotate(rotation).translate(position)
        hash = getMatrixHash()
        return if (parent != null) {
            matrix.mul(parent!!.generateMatrix())
        } else {
            matrix
        }.also { cached.set(matrix) }
    }

    fun getMatrixHash(): Int {
        return position.hashCode() + rotation.hashCode() + scale.hashCode()
    }

    override fun toJson(): Any? {
        return mapOf(
            "position" to position.toJson(),
            "rotation" to rotation.toJson(),
            "scale" to scale.toJson()
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        position = Vector3fExt.fromJson(map["position"])
        rotation = QuaternionfExt.fromJson(map["rotation"])
        scale = Vector3fExt.fromJson(map["scale"])

        generateMatrix()
    }
}
