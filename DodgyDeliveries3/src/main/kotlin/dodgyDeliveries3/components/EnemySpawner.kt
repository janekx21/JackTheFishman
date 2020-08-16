package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.*

class EnemySpawner : Component() {
    var timer = 0f

    override fun update() {
        timer -= Time.deltaTime
        if (timer <= 0) {
            timer = spawnInterval
            spawn()
        }
    }

    private fun spawn() {
        Scene.active.spawn(makeStandardEnemy(Vector3f(0f, 5f, -20f)))
    }

    private fun makeStandardEnemy(position: Vector3fc): GameObject {
        return GameObject("StandardEnemy").also { gameObject ->
            gameObject.addComponent<Transform>().also {
                it.position = position
                it.scale = Vector3fConst.one * .5f
            }
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/standardenemy.fbx")
            }
            gameObject.addComponent<ProjectileSpawner>()
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = 1f
                it.moves = LinkedList(EnemyCommander.MovementCommand.twirl)
            }
        }
    }

    companion object {
        const val spawnInterval = 20f
    }
}
