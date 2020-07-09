package components

import Component
import GameObject
import engine.audio.IPlayable
import engine.audio.Sample
import engine.audio.Source
import engine.math.Vector3fCopy

class AudioComponent(gameObject: GameObject) : Component(gameObject), IPlayable {
    private var source = Source()

    var sample: Sample? = null
        get() = source.sample
        set(value) {
            source.sample = value
            field = value
        }

    // TODO: AudioComponent state vom Source state trennen, da Components jetzt auch deaktiviert sein können
    // Die States sind zZ nicht getrennt, d.h. wenn play() bei einem deaktivierten components.AudioComponent aufgerufen wird
    // (das kann uA passieren wenn das Level gerade mit Scene.createViaPath geladen wird, oder gerade ein save-state
    // geladen wird)

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