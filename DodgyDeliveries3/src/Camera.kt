import engine.Window
import org.joml.Matrix4f

class Camera(gameObject: GameObject) : Component(gameObject) {
    private val matrix = Matrix4f().perspective(90f, Window.aspect, .1f, 100f)
    private var hash = 0
    private val cached = Matrix4f()

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

    companion object {
        var main: Camera? = null
    }
}
