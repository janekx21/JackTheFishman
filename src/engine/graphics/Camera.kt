package engine.graphics

import engine.Window
import engine.math.Vector
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL46

class Camera(var position: Vector, var pan: Float, var tilt: Float) {
    private var fov: Double = 1.0 / 90.0
    private val speed = .4f
    private var mouse: Pair<Float, Float> = Pair(0f, 0f)
    var deltaMouse = Pair(0f, 0f)

    fun update(win: Window) {
        var dir = Vector(0f, 0f, 0f)
        if (GLFW.glfwGetKey(win.windowPointer, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            dir = Vector(dir.x - speed, dir.y, dir.z)
        }
        if (GLFW.glfwGetKey(win.windowPointer, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            dir = Vector(dir.x + speed, dir.y, dir.z)
        }
        if (GLFW.glfwGetKey(win.windowPointer, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            dir = Vector(dir.x, dir.y, dir.z + speed)
        }
        if (GLFW.glfwGetKey(win.windowPointer, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            dir = Vector(dir.x, dir.y, dir.z - speed)
        }
        if (GLFW.glfwGetKey(win.windowPointer, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            dir = Vector(dir.x, dir.y - speed, dir.z)
        }
        if (GLFW.glfwGetKey(win.windowPointer, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            dir = Vector(dir.x, dir.y + speed, dir.z)
        }
        position = Vector(
            dir.x * win.deltaTime + position.x,
            dir.y * win.deltaTime + position.y,
            dir.z * win.deltaTime + position.z
        )
        val x = DoubleArray(1) { 0.0 }
        val y = DoubleArray(1) { 0.0 }
        GLFW.glfwGetCursorPos(win.windowPointer, x, y)
        deltaMouse = Pair(x[0].toFloat() - mouse.first, y[0].toFloat() - mouse.second)
        mouse = Pair(x[0].toFloat(), y[0].toFloat())


        tilt += deltaMouse.second * .1f
        pan += deltaMouse.first * .1f
    }

    fun matrix(aspect: Float) {
        GL46.glMatrixMode(GL46.GL_MODELVIEW)
        GL46.glLoadIdentity()
        GL11.glRotatef(tilt, 1f, 0f, 0f)
        GL11.glRotatef(pan, 0f, 1f, 0f)
        GL46.glTranslatef(position.x, position.y, position.z)

        GL46.glMatrixMode(GL46.GL_PROJECTION)
        GL46.glLoadIdentity()
        GL46.glFrustum(-fov * aspect, fov * aspect, -fov, fov, .01, 10.0);

        GL46.glMatrixMode(GL46.GL_MODELVIEW)
    }
}