import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.collision.shapes.CircleShape
import org.joml.Vector2f
import org.joml.Vector2fc

class PhysicsComponent(gameObject: GameObject, startPos: Vector2f, pos: Transform) : Component(gameObject) {

    //Body
    var definition: BodyDef = BodyDef()
    val tp: BodyType = BodyType.DYNAMIC

    //Collider is Circle cuz there is no box (maybe im just stupid and didnt find it)
    var col: CircleShape = CircleShape()

    //need a transform to attach the colliders position to
    var tr: Transform = pos

    init {
        definition.type = tp
        definition.gravityScale = 0f //we be fly so no need for that
        definition.position.set(startPos.x(), startPos.y())
    }

    //getter for velocity
    public val getSpeed get() = definition.linearVelocity

    //setter for velocity takes Vector2fc and converts it to box2d vector
    public fun setVel(vec: Vector2fc){
        definition.linearVelocity.set(vec.x(), vec.y())
    }

    //getter for collider
    public val getCollider get() = col

    //change collider size (size is the radius of the circle)
    public fun SetColliderSize(r: Float){
        col.radius = r
    }

    override fun update() {
        //To Do: apply body position to transform which is currently impossible due to transform.position protection level

        //set position to transforms position
        col.m_p.set(tr.position.x(), tr.position.y())
    }

    override fun draw() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}