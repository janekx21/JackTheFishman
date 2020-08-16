package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import org.jbox2d.dynamics.contacts.Contact

class Projectile : Component(), ICollisionHandler {

    val damage: Float = 1f

    override fun update() {
        if (transform.position.z() > 10f) {
            Scene.active.destroy(gameObject)
        }
    }

    override fun draw() {
    }

    override fun beginContact(ours: Collider, other: Collider, contact: Contact) {
        if(other.gameObject.name == "Player") {
            other.gameObject.getComponent<Health>().takeDamage(damage)
            Scene.active.destroy(gameObject)
        }
    }

    override fun endContact(ours: Collider, other: Collider, contact: Contact) {
    }
}
