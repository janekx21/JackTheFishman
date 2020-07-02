import engine.Time
import engine.math.*
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.collision.shapes.CircleShape
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector3f

abstract class ColliderComponent(gameObject: GameObject) : Component(gameObject) {

    //Body
    var definition: BodyDef = BodyDef()
    val dynamics: BodyType = BodyType.DYNAMIC

    //Collider is Circle cuz there is no box (maybe im just stupid and didnt find it)
    var collisionCircle: CircleShape = CircleShape()
    
    init {
        definition.type = dynamics
        definition.gravityScale = 0f
        //we be fly so no need for that
    }

    //getter for velocity
    public var velocity: Vector2fc
        get() = definition.linearVelocity.toVector2fc()
        set(value) {
            definition.linearVelocity.set(value.toVec2())
        }

    //getter for collider
    public val collider
        get() = collisionCircle


    //change collider size (size is the radius of the circle)
    public fun setColliderSize(r: Float){
        collisionCircle.radius = r
    }


    override fun update() {
        //set position to transforms position
        collisionCircle.m_p.set(transform.position.x(), transform.position.y())
        transform.position += Vector3f(definition.linearVelocity.x, 0F, definition.linearVelocity.y) * Time.deltaTime
    }

    override fun draw() {
    }
}