package dodgyDeliveries3.components

import org.jbox2d.dynamics.contacts.Contact

interface ICollisionHandler {
    fun beginContact(ours: Collider, other: Collider, contact: Contact)
    fun endContact(ours: Collider, other: Collider, contact: Contact)
}
