package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import org.jbox2d.dynamics.contacts.Contact

class Projectile(val damage: Float = 1f) : Component(), ICollisionHandler {

    override fun update() {
        if (transform.position.z() > 10f) {
            Scene.active.destroy(gameObject)
        }
    }

    override fun beginContact(ours: Collider, other: Collider, contact: Contact) {
        if(other.gameObject.getComponents<Player>().isNotEmpty()) {
            other.gameObject.getComponent<Health>().applyDamage(damage)
            Scene.active.destroy(gameObject)
        }
    }

    override fun endContact(ours: Collider, other: Collider, contact: Contact) {
    }
}
