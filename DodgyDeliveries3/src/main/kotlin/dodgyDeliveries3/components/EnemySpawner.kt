package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import jackTheFishman.engine.util.range
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.*
import kotlin.math.max
import kotlin.random.asKotlinRandom

class EnemySpawner : Component() {
    var timer = 0f
    var spawnInterval = 8f
    private val random = Random().asKotlinRandom()

    override fun update() {
        timer -= Time.deltaTime
        if (timer <= 0) {
            timer = spawnInterval
            spawn()
            spawnInterval = max(spawnInterval - 1f, 1f)
        }
    }

    private fun spawn() {
        val xOffset = random.range(-2f, 2f)
        val timerOffset = random.range(0f, 1f)
        if (random.nextFloat() > .5f) {
            Scene.active.spawn(makeStandardEnemy(Vector3f(xOffset, 5f, -20f), timerOffset))
        } else {
            val right = random.nextFloat() > .5f
            Scene.active.spawn(makeHammerhead(Vector3f(xOffset, 5f, -10f), timerOffset, right))
        }
    }

    private fun makeStandardEnemy(position: Vector3fc, timeOffset: Float): GameObject {
        return GameObject("Standard Enemy").also { gameObject ->
            gameObject.addComponent<Transform>().also {
                it.position = position
                it.scale = Vector3fConst.one * .5f
            }
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/standardenemy.fbx")
                material = material.copy(albedoTexture = Loader.createViaPath<Texture2D>("textures/stripes.png"))
            }
            gameObject.addComponent<ProjectileSpawner>().also {
                it.timer = timeOffset
            }
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = 1f
                it.moves = LinkedList(EnemyCommander.MovementCommand.twirl)
            }
        }
    }

    private fun makeHammerhead(position: Vector3fc, timeOffset: Float, right: Boolean): GameObject {
        return GameObject("Hammerhead Enemy").also { gameObject ->
            gameObject.addComponent<Transform>().also {
                it.position = position
                it.scale = Vector3fConst.one * .5f
            }
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/hammerheadenemy.fbx")
            }
            gameObject.addComponent<ProjectileSpawner>().also {
                it.type = ProjectileSpawner.Type.WOBBLE
                it.timer = timeOffset
            }
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = 1f
                if (right) {
                    it.moves = LinkedList(EnemyCommander.MovementCommand.shortRight)
                } else {
                    it.moves = LinkedList(EnemyCommander.MovementCommand.shortLeft)
                }
            }
        }
    }
}
