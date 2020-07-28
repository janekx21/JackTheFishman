package jackTheFishman.engine

object Time {
    var time = 0f
    var deltaTime = 0f

    var timeScale = 1f

    fun update(deltaTime: Float) {
        Time.deltaTime = deltaTime
        time += deltaTime * timeScale
    }
}