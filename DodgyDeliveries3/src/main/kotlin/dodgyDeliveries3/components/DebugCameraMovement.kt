package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Input.Keyboard.down
import jackTheFishman.engine.Input.Keyboard.justDown
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.plusAssign
import jackTheFishman.engine.math.times
import org.joml.Vector3f
import org.joml.Vector3fc
import org.lwjgl.glfw.GLFW.*

class DebugCameraMovement(var active: Boolean = false) : Component() {
    override fun update() {
        if (active) {
            transform.position = transform.position + currentDirection() * (currentSpeed() * Time.deltaTime)
        }
        if (justDown(GLFW_KEY_F3)) {
            active = !active
        }
    }

    private fun currentDirection(): Vector3fc {
        val move = Vector3f(Vector3fConst.zero)
        for ((key, direction) in keyToDirection) {
            if (down(key)) {
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
        if (down(GLFW_KEY_LEFT_SHIFT)) {
            speed *= 5f
        }
        return speed
    }

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
