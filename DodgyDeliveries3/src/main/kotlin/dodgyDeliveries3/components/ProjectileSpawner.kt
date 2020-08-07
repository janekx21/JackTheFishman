package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Loader
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector2fConst
import jackTheFishman.engine.math.times
import org.joml.Vector3fc

class ProjectileSpawner : Component() {
    var projectileSpeed = 5f
    var projectilesPerSecond = 0.1f

    private var timer = 0f

    override fun update() {
        if (timer >= 1/projectilesPerSecond) {
            Scene.active.spawn(makeProjectile(transform.position, projectileSpeed))
            timer = 0f
        }
        timer += Time.deltaTime
    }

    fun makeProjectile(startPosition: Vector3fc, movementSpeed: Float) : GameObject {
        return GameObject("Projectile").also {
            it.addComponent<Transform>().apply {
                transform.position = startPosition
            }
            it.addComponent<ModelRenderer>().apply {
                mesh = Loader.createViaPath("models/cube.fbx")
            }
            it.addComponent<CircleCollider>().apply {
                velocity = Vector2fConst.up * movementSpeed
                isSensor = true
            }
            it.addComponent<Projectile>()
        }
    }

    override fun draw() {
    }
}
