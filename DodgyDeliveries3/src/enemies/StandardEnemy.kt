package enemies

import GameObject
import ProjectileSpawner
import components.BoxCollider
import components.ModelRenderer
import components.Transform
import engine.Loader
import engine.graphics.Mesh
import org.joml.Vector2f
import org.joml.Vector3f

class StandardEnemy : GameObject("standardenemy") {
    init {
        addComponent<ProjectileSpawner>().also {
            it.shotsPerSecond = 0.5f
            it.projectileSpeed = 3f
            it.projectileDamage = 20f
        }
        addComponent<Transform>().also {
            it.position = Vector3f(0f, 0f, -10f)
        }
        addComponent<BoxCollider>().also {
            it.isSensor = true
            it.velocity = Vector2f(1f, 0f)
        }
        addComponent<ModelRenderer>().also {
            it.mesh = Loader.createViaPath(Mesh, "models/monkey.fbx")
        }
    }
}