import engine.Window
import engine.math.fromJson
import engine.math.toJson
import org.joml.Matrix4f

class Camera(gameObject: GameObject) : Component(gameObject) {
    private var matrix = Matrix4f().perspective(90f, Window.aspect, .1f, 100f)
    private var hash = 0
    private var cached = Matrix4f()

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

    override fun toJson(): Any? {
        return matrix.toJson()
    }

    override fun fromJson(json: Any?) {
        this.matrix = Matrix4f().also { it.fromJson(json) }
        generateViewMatrix()
    }
}
