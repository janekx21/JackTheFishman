package dodgyDeliveries3

import dodgyDeliveries3.prefabs.*
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Input
import jackTheFishman.engine.math.times
import org.joml.Vector3f

fun loadDefaultScene() {
    removeAllGameObjects()
    config()
    makeDefaultScene().forEach { Scene.active.spawn(it) }
}

fun removeAllGameObjects() {
    for (gameObject in Scene.active.allGameObjects) {
        Scene.active.destroy(gameObject)
    }
}

fun config() {
    Input.Mouse.setMode(Input.Mouse.CursorMode.HIDDEN)
}

fun makeDefaultScene(): Array<GameObject> {
    val (player, box) = makePlayerWithBox()
    return arrayOf(
        makePauseMenuOpener(),
        makeTrack1(),
        player,
        box,
        makeEnemySpawner(),
        makeTunnel(50f),
        makeTunnel(-50f),
        makeCamera(player.transform),
        makeLight(Vector3f(0f, 0f, 0f), Vector3f(ColorPalette.BLUE) * 2f),
        makeLight(Vector3f(0f, 0f, 10f), Vector3f(ColorPalette.ORANGE)),
        GameObject("HealthIndicator").also { gameObject ->
            // gameObject.addComponent<HealthIndicator>()
        }
    )
}
