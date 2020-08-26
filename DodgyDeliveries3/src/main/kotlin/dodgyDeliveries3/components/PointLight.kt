package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import dodgyDeliveries3.util.ColorPalette
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.moveTowards
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times
import org.joml.Vector3f
import org.joml.Vector3fc
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

data class PointLight(var color: Vector3fc = Vector3f(0f, 0f, 0f)) : Component() {
    var animatedColor: Vector3fc = Vector3fConst.zero
    var targetColor: Vector3fc = Vector3fConst.zero
    var alive = true

    override fun start() {
        all.add(this)
    }

    override fun update() {
        val beat = Scene.active.findViaName("Music").getComponent<Music>().beat
        val timeTillBeat = (cos(beat % 1f * 2 * PI) * .5 + .5).toFloat()

        targetColor = (if (alive) color else Vector3fConst.zero) + ColorPalette.WHITE * timeTillBeat.pow(10) * 4f
        animatedColor = animatedColor.moveTowards(targetColor, Time.deltaTime / colorSwitchTime)
    }

    override fun draw() {
        if (Debug.active) {
            Debug.drawWiredSphere(transform.position, .1f, color)
        }
    }

    override fun stop() {
        alive = false
    }

    companion object {
        const val max = 32
        val all = arrayListOf<PointLight>()
        const val colorSwitchTime = .00002f
    }
}
