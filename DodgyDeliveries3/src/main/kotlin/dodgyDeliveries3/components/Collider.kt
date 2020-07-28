package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.math.toVector2fc
import org.jbox2d.dynamics.Fixture
import org.joml.Vector2fc
import org.joml.Vector3f

abstract class Collider : Component() {
    protected abstract val fixture: Fixture
    abstract var velocity: Vector2fc

    override fun update() {
        //set position to transforms position
        val bodyPosition = fixture.body.position.toVector2fc()
        transform.position = Vector3f(bodyPosition.x(), 0f, bodyPosition.y())
    }

    override fun draw() {
    }
}