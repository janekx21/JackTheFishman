import engine.math.Vector3fCopy
import engine.math.fromJson
import engine.math.toJson
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Transform(gameObject: GameObject) : Component(gameObject) {
    var position = Vector3fCopy.zero
    var rotation = Quaternionf()
    var scale = Vector3fCopy.one
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

        position = Vector3f().also { it.fromJson(map["position"]) }
        rotation = Quaternionf().also { it.fromJson(map["rotation"]) }
        scale = Vector3f().also { it.fromJson(map["scale"]) }

        generateMatrix()
    }
}