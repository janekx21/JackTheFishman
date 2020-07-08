package components

import Component
import GameObject
import engine.math.toVec2
import engine.math.toVector2fc
import org.jbox2d.dynamics.Fixture
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f

abstract class Collider(gameObject: GameObject) : Component(gameObject) {
    protected abstract val fixture: Fixture

    /**
     * getter for velocity
     */
    var velocity: Vector2fc
        get() = fixture.body.linearVelocity.toVector2fc()
        set(value) {
            fixture.body.linearVelocity.set(value.toVec2())
        }

    var isSensor: Boolean
        get() = fixture.isSensor
        set(value) {
            fixture.isSensor = value
        }

    var linearDamping: Float
        get() = fixture.body.linearDamping
        set(value) {
            fixture.body.linearDamping = value
        }

    fun applyForceToCenter(value: Vector2f) {
        fixture.m_body.applyForceToCenter(value.toVec2())
    }

    override fun update() {
        //set position to transforms position
        val bodyPosition = fixture.body.position.toVector2fc()
        transform.position = Vector3f(bodyPosition.x(), 0f, bodyPosition.y())
    }

    override fun draw() {
    }
}