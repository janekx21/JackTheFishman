package graphics

import Input
import Time
import Window
import math.Vector3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL46

class Camera(var position: Vector3, var pan: Float, var tilt: Float) {
    private var fov: Double = 1.0 / 90.0
    private val speed = .4f

    init {
        Input.Mouse.setMode(GLFW_CURSOR_DISABLED)
    }

    fun update() {
        var dir = Vector3.zero
        if (Input.Keyboard.down(GLFW_KEY_D)) {
            dir += Vector3.right
        }
        if (Input.Keyboard.down(GLFW_KEY_A)) {
            dir -= Vector3.right
        }
        if (Input.Keyboard.down(GLFW_KEY_W)) {
            dir += Vector3.forward
        }
        if (Input.Keyboard.down(GLFW_KEY_S)) {
            dir -= Vector3.forward
        }
        if (Input.Keyboard.down(GLFW_KEY_SPACE)) {
            dir += Vector3.up
        }
        if (Input.Keyboard.down(GLFW_KEY_LEFT_SHIFT)) {
            dir -= Vector3.up
        }
        position += dir.normalized() * speed * Time.deltaTime
        val x = doubleArrayOf(0.0)
        val y = doubleArrayOf(0.0)
        glfwGetCursorPos(Window.pointer, x, y)

        tilt += Input.Mouse.deltaPosition.y * .1f
        pan += Input.Mouse.deltaPosition.x * .1f
    }

    fun matrix(aspect: Float) {
        GL46.glMatrixMode(GL46.GL_MODELVIEW)
        GL46.glLoadIdentity()
        GL11.glRotatef(tilt, 1f, 0f, 0f)
        GL11.glRotatef(pan, 0f, 1f, 0f)
        GL46.glTranslatef(-position.x, -position.y, -position.z)

        GL46.glMatrixMode(GL46.GL_PROJECTION)
        GL46.glLoadIdentity()
        GL46.glFrustum(-fov * aspect, fov * aspect, -fov, fov, .01, 10.0)

        GL46.glMatrixMode(GL46.GL_MODELVIEW)
    }
}