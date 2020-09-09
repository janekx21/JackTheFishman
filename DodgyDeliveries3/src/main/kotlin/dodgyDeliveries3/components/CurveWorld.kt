package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import kotlin.random.Random

enum class Curving {
    IDLE, TOWARDS, BACKWARDS
}

class CurveWorld : Component() {
    val interval = 10f
    val bendSpeed = 0.2f
    var state = Curving.IDLE

    var timer = 0f
    var target = 0.1f


    override fun update() {
        if (timer >= interval) {
            when (state) {
                Curving.IDLE -> {
                    target = calculateTarget()
                    state = Curving.TOWARDS
                    timer = 0f
                }
                Curving.TOWARDS -> {
                    state = Curving.BACKWARDS
                    timer = 0f
                }
                Curving.BACKWARDS -> {
                    state = Curving.IDLE
                    timer = 0f
                }
            }
        } else {
            when (state) {
                Curving.TOWARDS -> {
                    interpolate(target)
                }
                Curving.BACKWARDS -> {
                    interpolate(0f)
                }
                else -> {
                    if (Camera.main!!.curveWorld != 0f) {
                        interpolate(0f)
                    }
                }
            }
        }
        timer += Time.deltaTime
    }

    private fun calculateTarget(): Float {
        return Random.nextInt(-7, 7) * 0.001f
    }

    private fun interpolate(target: Float) {
        Camera.main!!.curveWorld =
            Camera.main!!.curveWorld + (target - Camera.main!!.curveWorld) * bendSpeed * Time.deltaTime
    }
}
