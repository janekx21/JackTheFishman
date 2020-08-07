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
        val matrix = Matrix4f().translate(position).scale(scale).rotate(rotation)
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
}
