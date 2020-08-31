package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import jackTheFishman.engine.util.range
import org.joml.Vector3f
import org.joml.Vector3fc
import java.util.*
import kotlin.random.asKotlinRandom

class EnemySpawner : Component() {
    var timer = 0f
    var spawnInterval = 4f
    private val random = Random().asKotlinRandom()

    override fun update() {
        timer -= Time.deltaTime
        if (timer <= 0) {
            timer = spawnInterval
            spawn()
        }
    }

    private fun spawn() {
        val xOffset = random.range(-2f, 2f)
        val timerOffset = random.range(0f, 1f)
        when ((random.nextFloat() * 4f).toInt()) {
            0 -> Scene.active.spawn(makeStandardEnemy(Vector3f(xOffset, 5f, -20f), timerOffset))
            1 -> {
                val right = random.nextFloat() > .5f
                Scene.active.spawn(makeHammerhead(Vector3f(xOffset, 5f, -15f), timerOffset, right))
            }
            2 -> Scene.active.spawn(makeLaserShark(Vector3f(xOffset, 5f, -15f), timerOffset))
            3 -> Scene.active.spawn(makeLanternFish(Vector3f(xOffset, 5f, -15f), timerOffset))

            else -> throw NotImplementedError("No fitting choice found")
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
                material = material.copy(
                    albedoColor = ColorPalette.RED,
                    normalTexture = Loader.createViaPath<Texture2D>("textures/sandNormal.png"),
                    normalIntensity = .1f
                )
            }
            gameObject.addComponent<ProjectileSpawner>().also {
                it.timer = timeOffset
                it.projectilesPerSecond = .3f
            }
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = 1.5f
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
                material = material.copy(
                    albedoColor = ColorPalette.YELLOW,
                    normalTexture = Loader.createViaPath<Texture2D>("textures/sandNormal.png"),
                    normalIntensity = .1f
                )
            }
            gameObject.addComponent<ProjectileSpawner>().also {
                it.type = ProjectileSpawner.Type.WOBBLE
                it.timer = timeOffset
                it.projectilesPerSecond = .2f
            }
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = 1.5f
                if (right) {
                    it.moves = LinkedList(EnemyCommander.MovementCommand.shortRight)
                } else {
                    it.moves = LinkedList(EnemyCommander.MovementCommand.shortLeft)
                }
            }
        }
    }

    private fun makeLaserShark(position: Vector3fc, timeOffset: Float): GameObject {
        return GameObject("Laser Shark Enemy").also { gameObject ->
            gameObject.addComponent<Transform>().also {
                it.position = position
                it.scale = Vector3fConst.one * .5f
            }
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/standardenemy.fbx")
                material = material.copy(
                    albedoTexture = Loader.createViaPath<Texture2D>("textures/stripes.png"),
                    normalTexture = Loader.createViaPath<Texture2D>("textures/sandNormal.png"),
                    normalIntensity = .1f
                )
            }
            gameObject.addComponent<ProjectileSpawner>().also {
                it.timer = timeOffset
                it.projectilesPerSecond = .5f
            }
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = .6f
                it.moves = LinkedList(EnemyCommander.MovementCommand.twirl)
            }
        }
    }

    private fun makeLanternFish(position: Vector3fc, timeOffset: Float): GameObject {
        return GameObject("Lantern Fish Enemy").also { gameObject ->
            gameObject.addComponent<Transform>().also {
                it.position = position
                it.scale = Vector3fConst.one * .5f
            }
            gameObject.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/lanternfishenemy.fbx")
                material = material.copy(
                    albedoColor = ColorPalette.BLUE,
                    normalTexture = Loader.createViaPath<Texture2D>("textures/sandNormal.png"),
                    normalIntensity = .1f
                )
            }
            gameObject.addComponent<ProjectileSpawner>().also {
                it.timer = timeOffset
                it.type = ProjectileSpawner.Type.LIGHT
                it.projectilesPerSecond = .2f
            }
            gameObject.addComponent<EnemyCommander>().also {
                it.speed = .6f
                it.moves = LinkedList(EnemyCommander.MovementCommand.twirl)
            }
        }
    }
}
