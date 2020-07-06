package components

import GameObject
import engine.Physics
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.FixtureDef
import org.joml.Vector3f
import util.Debug

class CircleCollider(gameObject: GameObject) : Collider(gameObject) {
    override val fixture: Fixture = Physics.world.createBody(BodyDef().apply {
        type = BodyType.DYNAMIC
        position = Vec2(transform.position.x(), transform.position.z())
    }).createFixture(FixtureDef().apply {
        friction = .3f
        density = 1f
        shape = CircleShape().apply {
            radius = 1f
        }
    })

    /**
     * change collider size (size is the radius of the circle)
     */
    var radius: Float = 1f
        set(value) {
            field = value
            fixture.shape.radius = value
        }

    override fun draw() {
        if (Debug.active) {
            super.draw()
            Debug.drawWiredSphere(transform.position, 1f, Vector3f(1f, 0f, 1f))
        }
    }
}