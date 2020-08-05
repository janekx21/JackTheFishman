package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times

class Tunnel(val speed: Float = 5f) : Component() {

    override fun update() {
        transform.position += Vector3fConst.backwards * Time.deltaTime * speed
        if (transform.position.z() > 50f) {
            transform.position += Vector3fConst.forward * 200f
        }
    }

    override fun draw() {
    }

}
