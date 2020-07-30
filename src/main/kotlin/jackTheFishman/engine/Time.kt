package jackTheFishman.engine

/**
 * Object that directly controls global time
 */
object Time {
    /**
     * The amount of time since that creation of time (aka game start)
     */
    var time = 0f

    /**
     * The amount of time that has passed since the last time update
     */
    var deltaTime = 0f

    var timeScale = 1f

    fun update(deltaTime: Float) {
        check(deltaTime >= 0f) { "Time can not flow in reverse" }
        Time.deltaTime = deltaTime
        time += deltaTime * timeScale
    }
}
