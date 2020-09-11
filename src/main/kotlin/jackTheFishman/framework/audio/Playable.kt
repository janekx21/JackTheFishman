package jackTheFishman.framework.audio

/**
 * Represents a resource that can be played
 */
interface Playable {
    /** Starts play head */
    fun play()

    /** Pauses play head */
    fun pause()

    /** Resets and pauses the play head */
    fun stop()
    var timeInSeconds: Float
    val isPlaying: Boolean
}
