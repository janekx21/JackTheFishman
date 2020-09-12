package jackTheFishman

interface Time {
    /**
     * Time since window open in seconds
     */
    var time: Float

    /**
     * The amount of time that has passed since the last update in seconds
     */
    var deltaTime: Float

    /**
     * Scale of time where
     * 1 or 100% corresponds to normal time flow.
     */
    var timeScale: Float
    fun update(passedTime: Float)
}
