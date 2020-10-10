package jackTheFishman

import kotlin.math.min

/**
 * Directly controls global time
 */
class WindowTime(window: Window) : Time {
    init {
        window.onUpdate.subscribe { timeInSeconds ->
            val passedTime = timeInSeconds - time
            check(passedTime >= 0f) { "Time can not flow in reverse" }
            val clampedPassedTime = min(passedTime, MAX_DELTA_TIME)
            deltaTime = clampedPassedTime * timeScale
            time += deltaTime
        }
    }

    override var time = 0f

    override var deltaTime = 0f

    override var timeScale = 1f

    companion object {
        private const val MAX_DELTA_TIME = .1f // translates to 10fps
    }
}
