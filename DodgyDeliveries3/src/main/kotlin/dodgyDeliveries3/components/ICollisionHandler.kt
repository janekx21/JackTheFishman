package dodgyDeliveries3.components

import dodgyDeliveries3.GameObject

interface ICollisionHandler {
    fun beginContact(other: GameObject)
    fun endContact(other: GameObject)
}
