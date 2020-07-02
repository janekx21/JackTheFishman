package components

import Component
import GameObject
import engine.Physics
import engine.math.toVec2
import engine.math.toVector2fc
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.joml.Vector2fc
import org.joml.Vector3f

class BoxCollider(gameObject: GameObject) : Component(gameObject) {
    private val body: Body = Physics.world.createBody(BodyDef().apply {
        type = BodyType.DYNAMIC
        position = Vec2(1f, 0f)
    })
    private val shape = PolygonShape().apply {
        setAsBox(50f, 10f)
    }
    private val fixture = body.createFixture(FixtureDef().apply {
        friction = .3f
        density = 1f
        shape = this@BoxCollider.shape
    })

    var velocity: Vector2fc
        get() = body.linearVelocity.toVector2fc()
        set(value) {
            body.linearVelocity.set(value.toVec2())
        }

    override fun update() {
        val bodyPosition = fixture.body.position.toVector2fc()
        transform.position.set(Vector3f(bodyPosition.x(), 0f, bodyPosition.y()))
    }

    override fun draw() {
    }
}