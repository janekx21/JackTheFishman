import components.Collider
import org.jbox2d.callbacks.ContactImpulse
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.contacts.Contact

object ContactListener : ContactListener {
    override fun endContact(contact: Contact?) {
    }

    override fun beginContact(contact: Contact?) {
        check(contact != null)
        val first = getCollider(contact.fixtureA)
        val second = getCollider(contact.fixtureB)
        checkProjectileCollision(first, second)
    }

    override fun preSolve(contact: Contact?, manifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, contactImpulse: ContactImpulse?) {
    }

    private fun getCollider(fixture: Fixture): Collider {
        check(fixture.userData is Collider) {
            "Your userdata is not a collider"
        }
        return fixture.userData as Collider
    }

    // TODO: NEEDS REWORK
    private fun checkProjectileCollision(first: Collider, second: Collider) {
        if (first.gameObject.name == "player" || second.gameObject.name == "player") {
            if (first.gameObject.name == "projectile") {
                first.gameObject.getComponent<Projectile>().damagePlayer()
            } else if (second.gameObject.name == "projectile") {
                second.gameObject.getComponent<Projectile>().damagePlayer()
            }
        }
    }

}