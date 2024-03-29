package dodgyDeliveries3.components

import com.beust.klaxon.Json
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Physics
import jackTheFishman.engine.math.toVec2
import jackTheFishman.engine.math.toVector2fc
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.FixtureDef
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f

class BoxCollider(
    var internalVelocity: Vector2fc = Vector2f(0f, 0f),
    var internalIsSensor: Boolean = false,
    var internalLinearDamping: Float = 0f
) : Collider() {
    override val fixture: Fixture =
        Physics.world.createBody(
            BodyDef().also {
                it.type = BodyType.DYNAMIC
                it.linearVelocity = internalVelocity.toVec2()
                it.linearDamping = internalLinearDamping
            }
        ).createFixture(
            FixtureDef().also {
                it.friction = .3f
                it.density = 1f
                it.shape = PolygonShape().also { shape ->
                    shape.setAsBox(1f, 1f)
                }
                it.isSensor = internalIsSensor
                it.userData = this
            }
        )

    @Json(ignored = true)
    override var velocity: Vector2fc
        get() {
            return fixture.body.linearVelocity.toVector2fc()
        }
        set(value) {
            fixture.body.isAwake = true
            fixture.body.linearVelocity.set(value.toVec2())
            internalVelocity = value
        }

    @Json(ignored = true)
    override var linearDamping: Float
        get() {
            return fixture.body.linearDamping
        }
        set(value) {
            fixture.body.linearDamping = value
            internalLinearDamping = value
        }

    @Json(ignored = true)
    override var isSensor: Boolean
        get() {
            return fixture.isSensor
        }
        set(value) {
            fixture.isSensor = value
            internalIsSensor = value
        }

    var size: Vector2fc = Vector2f(1f, 1f)
        set(value) {
            field = value
            fixture.m_shape = PolygonShape().apply {
                setAsBox(value.x(), value.y())
            }
        }

    override fun start() {
        fixture.body.setTransform(Vec2(transform.position.x(), transform.position.z()), fixture.body.angle)
        fixture.body.linearVelocity = velocity.toVec2()
        fixture.isSensor = isSensor
    }

    override fun draw() {
        if (Debug.active) {
            super.draw()
            Debug.drawWiredCube(transform.position, Vector3f(size.x(), 0f, size.y()), Vector3f(1f, 0f, 1f))
        }
    }
}
