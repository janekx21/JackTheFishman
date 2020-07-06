package components

import Component
import GameObject
import engine.Window
import org.joml.Matrix4f
import org.joml.Matrix4fc

class Camera(gameObject: GameObject) : Component(gameObject) {
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
        val matrix = Matrix4f(gameObject.transform.generateMatrix()).invert()
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
