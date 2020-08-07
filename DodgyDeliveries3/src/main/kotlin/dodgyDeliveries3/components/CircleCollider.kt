package dodgyDeliveries3.components

import com.beust.klaxon.Json
import dodgyDeliveries3.util.Debug
import jackTheFishman.engine.Physics
import jackTheFishman.engine.math.toVec2
import jackTheFishman.engine.math.toVector2fc
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.FixtureDef
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f

class CircleCollider(var internalVelocity: Vector2fc = Vector2f(0f, 0f), var internalRadius: Float = 1f) : Collider() {
    override val fixture: Fixture =
        Physics.world.createBody(
            BodyDef().also {
                it.type = BodyType.DYNAMIC
                it.linearVelocity = internalVelocity.toVec2()
            }
        ).createFixture(
            FixtureDef().also {
                it.friction = .3f
                it.density = 1f
                it.shape = CircleShape().also {
                    it.radius = internalRadius
                }
                it.userData = this
            }
        )

    @Json(ignored = true)
    override var velocity: Vector2fc
        get() {
            return fixture.body.linearVelocity.toVector2fc()
        }
        set(value) {
            fixture.body.linearVelocity.set(value.toVec2())
            internalVelocity = value
        }

    /**
     * change collider size (size is the radius of the circle)
     */
    @Json(ignored = true)
    var radius: Float
        get() = internalRadius
        set(value) {
            fixture.shape.radius = value
            internalRadius = value
        }


    override fun start() {
        fixture.body.position.set(Vec2(transform.position.x(), transform.position.z()))
        fixture.body.linearVelocity = velocity.toVec2()
    }

    override fun draw() {
        if (Debug.active) {
            super.draw()
            Debug.drawWiredSphere(transform.position, 1f, Vector3f(1f, 0f, 1f))
        }
    }
}
