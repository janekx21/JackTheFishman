package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import dodgyDeliveries3.components.Music
import dodgyDeliveries3.loadWinScreen
import jackTheFishman.engine.Input
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.audio.Sample

fun makeTrack1(): GameObject = makeTrack(
    Loader.createViaPath("music/Dream_Industrial.ogg"),
    offset = .029f,
    bpm = 110f
)

fun makeTrack2(): GameObject = makeTrack(
    Loader.createViaPath("music/Edge_of_Tomorrow.ogg"),
    offset = 0.05f,
    bpm = 90f
)

fun makeTrack3(): GameObject = makeTrack(
    Loader.createViaPath("music/Thaehan-Wind.ogg"),
    offset = 0.119f,
    bpm = 94f
)

fun makeTrack4(): GameObject = makeTrack(
    Loader.createViaPath("music/bensound-dreams.ogg"),
    offset = 0.689f,
    bpm = 95f
)

fun makeOwnTrack(sample: Sample, offset: Float, bpm: Float): GameObject = makeTrack(
    sample,
    offset = offset,
    bpm = bpm
)

fun makeTrack(sample: Sample, offset: Float, bpm: Float): GameObject {
    return GameObject("Music Track").also { gameObject ->
        gameObject.addComponent<Music>().also {
            it.sample = sample
            it.source.looping = false
            it.offset = offset
            it.bpm = bpm
            it.play()
            it.onEndReached = {
                Input.Mouse.setMode(Input.Mouse.CursorMode.NORMAL)
                val pauseOpener = Scene.active.findViaName("Pause")
                if (pauseOpener != null) {
                    Scene.active.destroy(pauseOpener)
                }
                Scene.active.spawn(loadWinScreen())
                Time.timeScale = 0f
            }
        }
    }
}
