import engine.Time
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.collision.shapes.CircleShape
import org.joml.Vector2f
import org.joml.Vector2fc

class PhysicsComponent(gameObject: GameObject) : Component(gameObject) {

    //Body
    var definition: BodyDef = BodyDef()
    val dynamics: BodyType = BodyType.DYNAMIC

    //Collider is Circle cuz there is no box (maybe im just stupid and didnt find it)
    var collisionCircle: CircleShape = CircleShape()

    //need a transform to attach the colliders position to
    var physicsTransform: Transform = Transform(gameObject)

    init {
        definition.type = dynamics
        definition.gravityScale = 0f
        //we be fly so no need for that
    }

    //getter for velocity
    public val Speed get() = definition.linearVelocity

    //setter for velocity takes Vector2fc and converts it to box2d vector
    public fun setVel(vec: Vector2fc){
        definition.linearVelocity.set(vec.x() * Time.deltaTime, vec.y() * Time.deltaTime)
    }

    //getter for collider
    public val Collider get() = collisionCircle

    //change collider size (size is the radius of the circle)
    public fun SetColliderSize(r: Float){
        collisionCircle.radius = r
    }

    override fun update() {
        //set position to transforms position
        collisionCircle.m_p.set(physicsTransform.position.x(), physicsTransform.position.y())
        physicsTransform.position.set(physicsTransform.position.x + definition.linearVelocity.x, physicsTransform.position.y + definition.linearVelocity.y, physicsTransform.position.z)
    }

    override fun draw() {
    }
}