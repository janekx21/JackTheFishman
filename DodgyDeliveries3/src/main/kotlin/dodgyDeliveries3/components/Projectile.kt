package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import dodgyDeliveries3.Scene

class Projectile : Component() {
    override fun update() {
        println("Projectile " + transform.position)
        if (transform.position.z() > 10f) {
            Scene.active.destroy(gameObject)
        }
    }

    override fun draw() {
    }
}
