package dodgyDeliveries3

import dodgyDeliveries3.prefabs.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Input
import jackTheFishman.engine.audio.Sample
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun loadDefaultScene(song: Song, difficulty: Difficulty) {
    removeAllGameObjects()
    config()
    makeDefaultScene(song, difficulty).forEach { Scene.active.spawn(it) }
}

fun removeAllGameObjects() {
    for (gameObject in Scene.active.allGameObjects) {
        Scene.active.destroy(gameObject)
    }
}

fun config() {
    Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
}

fun makeDefaultScene(song: Song, difficulty: Difficulty): Array<GameObject> {
    val (player, box) = makePlayerWithBox()

    val track = when (song) {
        Song.DREAMINDUSTRIAL -> makeTrack1()
        Song.EDGEOFTOMORROW -> makeTrack2()
        Song.WIND -> makeTrack3()
        Song.DREAMS -> makeTrack4()
    }

    val enemySpawner = when (difficulty) {
        Difficulty.EASY -> makeEasyEnemySpawner()
        Difficulty.HARD -> makeHardEnemySpawner()
    }

    return arrayOf(
        makePauseMenuOpener(),
        track,
        player,
        box,
        makeHealthIndicator(),
        enemySpawner,
        makeForwardTunnel(50f),
        makeForwardTunnel(-50f),
        makeGrid(),
        makeWorldCurver(),
        makeCamera(player.transform),
        makeLight(Vector3f(0f, 0f, 0f), Vector3f(ColorPalette.BLUE) * 2f),
        makeLight(Vector3f(0f, 0f, 10f), Vector3f(ColorPalette.ORANGE))
    )
}

fun loadDefaultSceneFromOwnSample(sample: Sample, offset: Float, bpm: Float, difficulty: Difficulty) {
    removeAllGameObjects()
    config()
    makeDefaultSceneFromOwnSample(sample, offset, bpm, difficulty).forEach { Scene.active.spawn(it) }
}

fun makeDefaultSceneFromOwnSample(
    sample: Sample,
    offset: Float,
    bpm: Float,
    difficulty: Difficulty
): Array<GameObject> {
    val (player, box) = makePlayerWithBox()

    val enemySpawner = when (difficulty) {
        Difficulty.EASY -> makeEasyEnemySpawner()
        Difficulty.HARD -> makeHardEnemySpawner()
    }

    return arrayOf(
        makePauseMenuOpener(),
        makeOwnTrack(sample, offset, bpm),
        player,
        box,
        makeHealthIndicator(),
        enemySpawner,
        makeForwardTunnel(50f),
        makeForwardTunnel(-50f),
        makeGrid(),
        makeCamera(player.transform),
        makeLight(Vector3f(0f, 0f, 0f), Vector3f(ColorPalette.BLUE) * 2f),
        makeLight(Vector3f(0f, 0f, 10f), Vector3f(ColorPalette.ORANGE))
    )
}
