package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import jackTheFishman.engine.audio.IPlayable
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.audio.Source
import jackTheFishman.engine.math.Vector3fCopy

class AudioComponent(gameObject: GameObject) : Component(gameObject), IPlayable {
    private var source = Source()

    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    override fun play() = source.play()

    override fun pause() = source.pause()

    override fun stop() = source.stop()

    override var time: Float
        set(value) { source.time = value }
        get() = source.time

    override val playing: Boolean get() = source.playing

    override fun update() {
        /// An dieser Stelle wäre es falsch, `transform.position` zu benutzen, da das
        /// nur die Translation eines einzelnen Transform Components wäre.
        /// Wir wollen aber die Translation aller Transform Componenten zusammengefasst haben.
        source.position = transform.generateMatrix().getTranslation(Vector3fCopy.zero)
    }

    override fun draw() {}

    override fun toJson(): Any? {
        return mapOf(
            "source" to source.toJson()
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        source = Source.fromJson(map["source"]!!)
    }
}