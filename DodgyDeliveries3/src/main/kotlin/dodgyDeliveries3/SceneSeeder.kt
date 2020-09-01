package dodgyDeliveries3

import dodgyDeliveries3.prefabs.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Input
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun loadDefaultScene(difficulty: Difficulty) {
    removeAllGameObjects()
    config()
    makeDefaultScene(difficulty).forEach { Scene.active.spawn(it) }
}

fun removeAllGameObjects() {
    for (gameObject in Scene.active.allGameObjects) {
        Scene.active.destroy(gameObject)
    }
}

fun config() {
    Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
}

fun makeDefaultScene(difficulty: Difficulty): Array<GameObject> {
    val (player, box) = makePlayerWithBox()

    val track = when (difficulty) {
        Difficulty.EASY -> makeTrack1()
        Difficulty.HARD -> makeTrack2()
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
        enemySpawner,
        makeForwardTunnel(50f),
        makeForwardTunnel(-50f),
        makeCamera(player.transform),
        makeLight(Vector3f(0f, 0f, 0f), Vector3f(ColorPalette.BLUE) * 2f),
        makeLight(Vector3f(0f, 0f, 10f), Vector3f(ColorPalette.ORANGE))
    )
}
