package dodgyDeliveries3.prefabs

import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import dodgyDeliveries3.components.Music
import jackTheFishman.engine.Loader

fun makeTrack1(): GameObject {
    return GameObject("Music Track 1").also { gameObject ->
        gameObject.addComponent<Music>().also {
            it.sample = Loader.createViaPath("music/Sia&SafriDuo-Chandelier(AlchimystRemix).ogg")
            it.offset = .082f
            it.bpm = 138f
            it.play()
        }
        Scene.active.spawn(gameObject)
    }
}

fun makeTrack2(): GameObject {
    return GameObject("Music Track 2").also { gameObject ->
        gameObject.addComponent<Music>().also {
            it.sample = Loader.createViaPath("music/Protonica-Apollo.ogg")
            it.offset = 14.054f
            it.bpm = 138f
            it.play()
        }
        Scene.active.spawn(gameObject)
    }
}
