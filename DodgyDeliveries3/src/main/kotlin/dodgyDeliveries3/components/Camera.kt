package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Window
import jackTheFishman.engine.math.unaryMinus
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Quaternionf

class Camera : Component() {
    private var matrix: Matrix4fc = Matrix4f().perspective(Math.toRadians(90.0).toFloat(), Window.aspect, .1f, 100f)
    private var hash = 0
    private var cached: Matrix4fc = Matrix4f()

    override fun update() {}

    override fun draw() {}

    fun getProjectionMatrix(): Matrix4fc {
        return matrix
    }

    fun updateProjectionMatrix() {
        matrix = Matrix4f().perspective(Math.toRadians(90.0).toFloat(), Window.aspect, .1f, 100f)
    }

    fun generateViewMatrix(): Matrix4fc {
        if (hash == getMatrixHash()) {
            return cached
        }
        val rotation = Quaternionf(transform.rotation).invert()
        val matrix = Matrix4f().rotate(rotation).translate(-transform.position)
        hash = getMatrixHash()
        return matrix.also { cached = matrix }
    }

    private fun getMatrixHash(): Int {
        return gameObject.transform.getMatrixHash()
    }

    companion object {
        var main: Camera? = null
    }
}
