package jackTheFishman

import kotlin.math.min

/**
 * Object that directly controls global time
 */
class WindowTime(private val window: Window) : Time {
    init {
        window.onBetweenUpdates.subscribe { timeInSeconds ->
            val passedTime = timeInSeconds - time
            check(deltaTime >= 0f) { "Time can not flow in reverse" }
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
