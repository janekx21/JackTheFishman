package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import dodgyDeliveries3.util.ColorPalette
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector2fConst
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.times
import org.joml.Vector3f
import org.joml.Vector3fc
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

class ProjectileSpawner(var timer: Float = 0f, var type: Type = Type.STANDARD) : Component() {
    enum class Type {
        STANDARD, WOBBLE, LIGHT
    }

    var projectilesPerSecond = 0.1f
    var musicComponent: Music? = null

    override fun start() {
        // TODO make it not name dependent
        musicComponent = Scene.active.findViaName("Music")?.getComponent()
        resetTimer()
    }

    override fun update() {
        val canShoot = gameObject.getComponent<EnemyCommander>().canShoot
        val timeout = 1f / projectilesPerSecond
        if (canShoot && timer >= timeout && isBeatCloseEnough()) {
            resetTimer()
            spawn()
        }
        timer += Time.deltaTime
    }

    private fun isBeatCloseEnough(): Boolean {
        if (musicComponent != null) {
            val beatClamped = musicComponent!!.beat % 1f
            val closenessToTheBeat = (cos(beatClamped * 2 * PI) * .5 + .5).toFloat()
            return closenessToTheBeat.pow(20) > .9f
        }
        return true
    }

    private fun resetTimer() {
        timer = 0f
    }

    private fun spawn() {
        when (type) {
            Type.STANDARD -> Scene.active.spawn(makeStandardProjectile(transform.position))
            Type.WOBBLE -> Scene.active.spawn(makeWobbleProjectile(transform.position))
            Type.LIGHT -> Scene.active.spawn(makeLightBall(transform.position))
        }
    }

    private fun makeStandardProjectile(startPosition: Vector3fc): GameObject {
        return GameObject("Standard Projectile").also {
            it.addComponent<Transform>().apply {
                transform.position = startPosition
                transform.scale = Vector3fConst.one * 0.2f
            }
            it.addComponent<ModelRenderer>().apply {
                material = material.copy(emissionColor = ColorPalette.RED * 1.5f)
                mesh = Loader.createViaPath("models/projectiles/standardenemyprojectile.fbx")
            }
            it.addComponent<CircleCollider>().apply {
                velocity = Vector2fConst.up * 15f
                isSensor = true
                radius = .2f
            }
            it.addComponent<PointLight>().apply {
                color = Vector3f(ColorPalette.RED) * 2f
            }
            it.addComponent<Projectile>().also { projectile ->
                projectile.damage = 1f
            }
            it.addComponent<Audio>().also { audio ->
                audio.sample = Loader.createViaPath("sounds/wind.ogg")
                audio.source.looping = true
                audio.source.gain = 0f
                audio.play()
            }
        }
    }

    private fun makeWobbleProjectile(startPosition: Vector3fc): GameObject {
        return GameObject("Wobble Projectile").also {
            it.addComponent<Transform>().apply {
                transform.position = startPosition
                transform.scale = Vector3fConst.one * 0.5f
            }
            it.addComponent<ModelRenderer>().apply {
                material = material.copy(emissionColor = ColorPalette.YELLOW * 1.5f)
                mesh = Loader.createViaPath("models/projectiles/hammerheadprojectile.fbx")
            }
            it.addComponent<CircleCollider>().apply {
                velocity = Vector2fConst.up * 8f
                isSensor = true
                radius = .5f
            }
            it.addComponent<PointLight>().apply {
                color = Vector3f(ColorPalette.YELLOW) * 2f
            }
            it.addComponent<WobbleProjectile>().also { projectile ->
                projectile.damage = 2f
            }
            it.addComponent<Audio>().also { audio ->
                audio.sample = Loader.createViaPath("sounds/wobble.ogg")
                audio.source.looping = true
                audio.source.gain = 0f
                audio.play()
            }
        }
    }

    private fun makeLightBall(startPosition: Vector3fc): GameObject {
        return GameObject("Light Ball Projectile").also {
            it.addComponent<Transform>().apply {
                transform.position = startPosition
                transform.scale = Vector3fConst.one * 0.2f
            }
            it.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/sphere.fbx")
                material = material.copy(
                    albedoColor = ColorPalette.BLUE,
                    emissionColor = ColorPalette.BLUE * 1.5f
                )
            }
            it.addComponent<CircleCollider>().apply {
                velocity = Vector2fConst.up * 5f
                isSensor = true
                radius = .2f
            }
            it.addComponent<PointLight>().apply {
                color = Vector3f(ColorPalette.BLUE) * 2f
            }
            it.addComponent<Projectile>().also { projectile ->
                projectile.damage = 3f
            }
            it.addComponent<Audio>().also { audio ->
                audio.sample = Loader.createViaPath("sounds/laser.ogg")
                audio.source.looping = true
                audio.source.gain = 0f
                audio.play()
            }
        }
    }
}
