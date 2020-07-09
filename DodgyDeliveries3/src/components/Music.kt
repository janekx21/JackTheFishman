package components

import Component
import GameObject
import engine.audio.IPlayable
import engine.audio.Sample
import engine.audio.Source

class Music(gameObject: GameObject) : Component(gameObject), IPlayable {
    /// TODO: Music state vom Source state trennen (siehe AudioComponent)

    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    var bpm = 100f
    var offset = 0f

    private var source = Source()

    override fun play() = source.play()
    override fun pause() = source.pause()
    override fun stop() = source.stop()
    override var time: Float
        set(value) { source.time = value }
        get() = source.time
    override val playing: Boolean get() = source.playing

    override fun update() {}

    override fun draw() {}

    val beat: Float
        get() = (time - offset) / (60f / bpm)

    override fun toJson(): Any? {
        return mapOf(
            "source" to source.toJson(),
            "bpm" to bpm,
            "offset" to offset
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        source = Source.fromJson(map["source"]!!)
        bpm = (map["bpm"] as Double).toFloat()
        offset = (map["offset"] as Double).toFloat()
    }
}