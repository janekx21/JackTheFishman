package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.components.Music
import jackTheFishman.engine.Loader
import jackTheFishman.engine.audio.Sample

fun makeTrack1(): GameObject = makeTrack(
    Loader.createViaPath("music/Sia&SafriDuo-Chandelier(AlchimystRemix).ogg"),
    offset = .082f,
    bpm = 138f
)

fun makeTrack2(): GameObject = makeTrack(
    Loader.createViaPath("music/Protonica-Apollo.ogg"),
    offset = 14.054f,
    bpm = 138f
)

fun makeTrack3(): GameObject = makeTrack(
    Loader.createViaPath("music/Thaehan-Wind.ogg"),
    offset = 0.119f,
    bpm = 94f
)

fun makeOwnTrack(path: String, offset: Float, bpm: Float): GameObject = makeTrack(
    Sample(Sample.getSampleFileViaPath(path)),
    offset = offset,
    bpm = bpm
)

fun makeTrack(sample: Sample, offset: Float, bpm: Float): GameObject {
    return GameObject("Music Track").also { gameObject ->
        gameObject.addComponent<Music>().also {
            it.sample = sample
            it.offset = offset
            it.bpm = bpm
            it.play()
        }
    }
}
