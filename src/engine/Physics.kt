package engine

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World

object Physics {
    var velocityIterations = 6
    var positionIterations = 2

    val world = World(Vec2(0f, 1f))

    fun update() {
        world.step(Time.deltaTime, velocityIterations, positionIterations)
    }
}