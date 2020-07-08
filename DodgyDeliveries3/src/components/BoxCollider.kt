package components

import GameObject
import jackTheFishman.engine.Physics
import jackTheFishman.engine.math.toJson
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.FixtureDef
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f
import util.Debug

class BoxCollider(gameObject: GameObject) : Collider(gameObject) {
    override val fixture: Fixture = Physics.world.createBody(BodyDef().apply {
        type = BodyType.DYNAMIC
        position = Vec2(transform.position.x(), transform.position.z())
    }).createFixture(FixtureDef().apply {
        friction = .3f
        density = 1f
        shape = PolygonShape().apply {
            setAsBox(1f, 1f)
        }
    })

    var size: Vector2fc = Vector2f(1f, 1f)
        set(value) {
            field = value
            fixture.m_shape = PolygonShape().apply {
                setAsBox(value.x(), value.y())
            }
        }

    override fun draw() {
        if (Debug.active) {
            super.draw()
            Debug.drawWiredCube(transform.position, Vector3f(size.x(), 0f, size.y()), Vector3f(1f, 0f, 1f))
        }
    }

    override fun toJson(): Any? {
        return mapOf(
            "size" to size.toJson()
        )
    }

    override fun fromJson(json: Any?) {
        TODO("Not yet implemented")
    }
}