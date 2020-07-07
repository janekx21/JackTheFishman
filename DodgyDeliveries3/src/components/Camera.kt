package components

import Component
import GameObject
import engine.Window
import engine.math.Vector3fCopy
import engine.math.minus
import engine.math.plus
import engine.math.times
import org.joml.*

class Camera(gameObject: GameObject) : Component(gameObject) {
    private var matrix: Matrix4fc = Matrix4f().perspective(Math.toRadians(90.0).toFloat(), Window.aspect, .1f, 100f)
    private var hash = 0
    private var cached: Matrix4fc = Matrix4f()

    var follow: Transform? = null

    var distance: Float = 0F

    var relativeRotation: Vector3f = Vector3f().zero()
    var smoothAmount: Float = 0F

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

    private fun smoothFollow() {
        if(follow != null){
            val pointToFollow = follow!!.position + relativeRotation.normalize(1F) * distance
            transform.position = Vector3f(transform.position).lerp(pointToFollow, smoothAmount)
            transform.rotation = (Quaternionf().identity().lookAlong((follow!!.position - transform.position), Vector3fCopy.up))
        }
    }

    companion object {
        var main: Camera? = null
    }
}
