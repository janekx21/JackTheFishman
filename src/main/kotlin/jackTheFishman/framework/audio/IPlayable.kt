package jackTheFishman.framework.audio

/**
 * Represents a resource that can be played
 */
interface IPlayable {
    fun play() // starts play head
    fun pause() // only pauses progress
    fun stop() // resets the play head
    var time: Float // in seconds
    val playing: Boolean
}
