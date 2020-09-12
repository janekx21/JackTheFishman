package jackTheFishman

import jackTheFishman.math.extentions.toVec2
import jackTheFishman.math.extentions.toVector2fc
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import org.joml.Vector2fc

object Physics {
    var velocityIterations = 6
    var positionIterations = 2

    val world = World(Vec2(0f, 0f))

    fun update() {
        world.step(Time.deltaTime, velocityIterations, positionIterations)
    }

    var gravity: Vector2fc
        get() = world.gravity.toVector2fc()
        set(value) {
            world.gravity = value.toVec2()
        }
}
