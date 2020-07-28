package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Input
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.*
import org.joml.Vector3f
import org.joml.Vector3fc
import org.lwjgl.glfw.GLFW.*

class DebugCameraMovement(var active: Boolean = false) : Component() {
    override fun update() {
        if (active) {
            transform.position = transform.position + currentDirection() * (currentSpeed() * Time.deltaTime)
        }
        if (Input.Keyboard.justDown(GLFW_KEY_F3)) {
            active = !active
        }
    }

    private fun currentDirection(): Vector3fc {
        val move = Vector3fCopy.zero
        for ((key, direction) in keyToDirection) {
            if (Input.Keyboard.down(key)) {
                move += Vector3f(direction)
            }
        }
        if (move.lengthSquared() > 0) {
            move.normalize()
        }
        return move
    }

    private fun currentSpeed(): Float {
        var speed = 10f
        if (Input.Keyboard.down(GLFW_KEY_LEFT_SHIFT)) {
            speed *= 5f
        }
        return speed
    }

    override fun draw() {}

    companion object {
        val keyToDirection = mapOf(
            GLFW_KEY_D to Vector3fConst.right,
            GLFW_KEY_A to Vector3fConst.left,
            GLFW_KEY_W to Vector3fConst.forward,
            GLFW_KEY_S to Vector3fConst.backwards,
            GLFW_KEY_SPACE to Vector3fConst.up,
            GLFW_KEY_LEFT_CONTROL to Vector3fConst.down
        )
    }
}
