import org.joml.Vector2f

class Projectile(gameObject: GameObject) : Component(gameObject) {

    // TODO: make abstract

    var damage: Float = 0f

    fun destroy() { // TODO: move this help function to gameobject class
        Scene.active.destroy(gameObject)
    }

    fun damagePlayer() {
        val playerHP = Scene.active.findViaName("player").getComponent<HealthComponent>()
        playerHP.takeDamage(damage)
        destroy()
    }

    override fun update() {
        if (Vector2f.distance(
                gameObject.transform.position.x(),
                gameObject.transform.position.z(),
                Player.transform.position.x(),
                Player.transform.position.y()
            ) > 100f
        ) {
            destroy()
        }
    }

    override fun draw() {
    }

    override fun toJson(): Any? {
        return mapOf(
            "damage" to damage
        )
    }

    override fun fromJson(json: Any?) {
        val map = json as Map<*, *>

        damage = (map["damage"] as Double).toFloat()
    }
}