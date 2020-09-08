package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene
import jackTheFishman.engine.math.minus
import org.jbox2d.dynamics.contacts.Contact

open class Projectile(var damage: Float = 1f) : Component(), ICollisionHandler {
    var internalAudio: Audio? = null
    private var volume: Float = 2f

    private val sound: Audio
        get() {
            check(internalAudio != null)
            return internalAudio!!
        }

    override fun start() {
        internalAudio = gameObject.getComponent()
    }

    override fun update() {
        if (transform.position.z() > 20f) {
            Scene.active.destroy(gameObject)
        }
        sound.source.gain =
            (1 / ((Camera.main!!.transform.position - gameObject.transform.position).length() * 2f)) * volume
    }

    override fun beginContact(ours: Collider, other: Collider, contact: Contact) {
        if (other.gameObject.getComponents<Player>().isNotEmpty()) {
            other.gameObject.getComponent<Health>().applyDamage(damage)
            Scene.active.destroy(gameObject)
        }
    }

    override fun endContact(ours: Collider, other: Collider, contact: Contact) {
    }
}
