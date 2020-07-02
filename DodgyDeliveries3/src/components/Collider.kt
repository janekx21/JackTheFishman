package components

import Component
import GameObject

abstract class Collider(gameObject: GameObject) : Component(gameObject) {
    /*
    //Body
    private val bodyDefinition: BodyDef = BodyDef()
    private val bodyType = BodyType.DYNAMIC
    protected val body: Body = Physics.world.createBody(bodyDefinition)
    protected abstract val shape: Shape

    init {
        bodyDefinition.type = bodyType
        //we be fly so no need gravity
        bodyDefinition.gravityScale = 0f
    }

    //getter for velocity
    public var velocity: Vector2fc
        get() = bodyDefinition.linearVelocity.toVector2fc()
        set(value) {
            bodyDefinition.linearVelocity.set(value.toVec2())
        }

    //getter for collider
    public val collider
        get() = collisionCircle


    //change collider size (size is the radius of the circle)
    public fun setColliderSize(r: Float) {
        collisionCircle.radius = r
    }


    override fun update() {
        //set position to transforms position
        collisionCircle.m_p.set(transform.position.x(), transform.position.y())
        transform.position += Vector3f(
            bodyDefinition.linearVelocity.x,
            0F,
            bodyDefinition.linearVelocity.y
        ) * Time.deltaTime
    }

     */
    override fun update() {
    }

    override fun draw() {
    }
}