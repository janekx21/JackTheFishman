object Time {
    var time = 0f
    var lastTime = 0f
    val deltaTime
        get() = time - lastTime

    fun update(currentTime: Float) {
        check(currentTime >= time) {"time cant flow backwards"}
        lastTime = time
        time = currentTime
    }
}