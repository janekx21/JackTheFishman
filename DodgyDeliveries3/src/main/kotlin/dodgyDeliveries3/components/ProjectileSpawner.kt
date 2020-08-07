package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.GameObject
import dodgyDeliveries3.Scene
import jackTheFishman.engine.Time
import jackTheFishman.engine.graphics.Mesh
import jackTheFishman.engine.graphics.Texture2D
import jackTheFishman.engine.math.Vector2fConst
import jackTheFishman.engine.math.times

class ProjectileSpawner : Component() {
    var projectileSpeed = 1f
    var timeDiff = 5f
    var projectileMesh: Mesh? = null
    var projectileTexture: Texture2D? = null

    private var time = 0f

    override fun update() {
        val goPosition = gameObject.transform.position
        println(time)
        if (time >= timeDiff) {
            Scene.active.spawn(GameObject("Projectile").also { projectile ->
                projectile.addComponent<Transform>().apply {
                    transform.position = goPosition
                }
                projectile.addComponent<ModelRenderer>().apply {
                    mesh = projectileMesh
                }
                projectile.addComponent<CircleCollider>().apply {
                    velocity = Vector2fConst.up * projectileSpeed
                }
                projectile.addComponent<Projectile>()
                Scene.active.spawn(projectile)
            })
            time = 0f
        }
        time += Time.deltaTime
    }

    override fun draw() {
    }

}
