package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.math.Vector3fConst
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Quaternionfc
import org.joml.Vector3fc

data class Transform(
    var position: Vector3fc = Vector3fConst.zero,
    var rotation: Quaternionfc = Quaternionf(),
    var scale: Vector3fc = Vector3fConst.one
) : Component() {
    var parent: Transform? = null

    private var hash = 0
    private val cached = Matrix4f()

    override fun update() {}
    override fun draw() {}

    fun generateMatrix(): Matrix4f {
        if (hash == getMatrixHash()) {
            return cached
        }
        hash = getMatrixHash()
        val matrix = Matrix4f()
        return if (parent != null) {
            matrix.mul(parent!!.generateMatrix()).translate(position).scale(scale).rotate(rotation)
        } else {
            matrix.translate(position).scale(scale).rotate(rotation)
        }.also { cached.set(matrix) }
    }

    fun getMatrixHash(): Int {
        return position.hashCode() + rotation.hashCode() + scale.hashCode() + if (parent != null) parent!!.getMatrixHash() else 0
    }
}
