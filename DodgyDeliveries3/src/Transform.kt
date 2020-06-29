import engine.math.Vector3fCopy
import org.joml.Matrix4f
import org.joml.Quaternionf

class Transform(gameObject: GameObject) : Component(gameObject) {
    val position = Vector3fCopy.zero
    val rotation = Quaternionf()
    val scale = Vector3fCopy.one
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
}