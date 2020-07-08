import components.CircleCollider
import components.Collider
import components.ModelRenderer
import components.Transform
import engine.Loader
import engine.Time
import engine.graphics.Mesh
import engine.math.times
import org.joml.Vector2f

class ProjectileSpawner(gameObject: GameObject) : Component(gameObject) {

    var shotsPerSecond = 0f
    var projectileSpeed = 0f
    var projectileDamage = 0f
    private var timer = 0f

    private fun spawnProjectile() {
        Scene.active.spawn(GameObject("projectile").also { go ->
            go.addComponent<Transform>().also {
                it.scale *= 0.5f
                it.position = gameObject.transform.position
            }
            go.addComponent<Projectile>().also {
                it.damage = projectileDamage
            }
            go.addComponent<CircleCollider>().also {
                it.velocity = Vector2f(0f, projectileSpeed)
                it.isSensor = true
            }
            go.addComponent<ModelRenderer>().also {
                it.mesh = Loader.createViaPath<Mesh>("models/sphere.fbx")
            }
        })
    }

    override fun update() {
        timer += Time.deltaTime

        if (timer > (1 / shotsPerSecond)) {
            spawnProjectile()
            timer -= (1 / shotsPerSecond)
        }

        //TODO: does not belong here / rename class to
        if (transform.position.x() > 4f) {
            gameObject.getComponent<Collider>().velocity = Vector2f(-1f, 0f)
        }
        if (transform.position.x() < -4f) {
            gameObject.getComponent<Collider>().velocity = Vector2f(1f, 0f)
        }
    }


    override fun draw() {}
}