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

class ProjectileSpawner : Component() {
    var projectileSpeed = 15f
    var projectilesPerSecond = 0.1f

    private var timer = 0f

    override fun update() {
        val canShoot = gameObject.getComponent<EnemyCommander>().canShoot
        if (canShoot && timer >= 1 / projectilesPerSecond) {
            Scene.active.spawn(makeProjectile(transform.position, projectileSpeed))
            timer = 0f
        }
        timer += Time.deltaTime
    }

    fun makeProjectile(startPosition: Vector3fc, movementSpeed: Float): GameObject {
        return GameObject("Projectile").also {
            it.addComponent<Transform>().apply {
                transform.position = startPosition
                transform.scale = Vector3fConst.one * 0.2f
            }
            it.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/projectiles/standardenemyprojectile.fbx")
            }
            it.addComponent<CircleCollider>().apply {
                velocity = Vector2fConst.up * movementSpeed
                isSensor = true
                radius = .2f
            }
            it.addComponent<PointLight>().apply {
                color = Vector3f(ColorPalette.GREEN) * 2f
            }
            it.addComponent<Projectile>().also { projectile ->
                projectile.damage = 10f
            }
        }
    }
}
