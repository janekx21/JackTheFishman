import engine.Window
import engine.math.Vector3fCopy
import engine.math.clamp
import engine.math.plus
import engine.math.times
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Quaternionf
import org.joml.Vector3f

class Camera(gameObject: GameObject) : Component(gameObject) {
    private val matrix = Matrix4f().perspective(Math.toRadians(90.0).toFloat(), Window.aspect, .1f, 100f)
    private var hash = 0
    private val cached = Matrix4f()

    var follow: Transform
        get() = follow
        set(value) {follow = value}

    var distance: Float
        get() = distance
        set(value) {distance = value}

    public var relativeRotation: Vector3f = Vector3f().zero()
    public var smoothAmount: Float = 0F

    override fun update() {}

    override fun draw() {}

    fun getProjectionMatrix(): Matrix4f {
        return matrix
    }

    fun generateViewMatrix(): Matrix4f {
        if (hash == getMatrixHash()) {
            return cached
        }
        val matrix = Matrix4f(gameObject.transform.generateMatrix()).invert()
        hash = getMatrixHash()
        return matrix.also { cached.set(matrix) }
    }

    private fun getMatrixHash(): Int {
        return gameObject.transform.getMatrixHash()
    }

    private fun smoothFollow() {
        val pointToFollow: Vector3f = follow.position + relativeRotation.normalize(1F) * distance
        transform.position.set(transform.position.lerp(pointToFollow, smoothAmount))
        transform.rotation.set(Quaternionf().identity().lookAlong((follow.position + transform.position * -1F), Vector3fCopy.up))
    }

    companion object {
        var main: Camera? = null
    }
}
