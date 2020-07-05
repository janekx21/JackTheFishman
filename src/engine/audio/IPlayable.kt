package engine.audio

interface IPlayable {
    fun play() // starts play head
    fun pause() // only pauses progress
    fun stop() // resets the play head
    val time: Float // in seconds
    val playing: Boolean
}