package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.plus
import jackTheFishman.engine.math.times

class Tunnel(var speed: Float = 5f, var forward: Boolean = true) : Component() {

    override fun update() {
        if(forward) {
            transform.position += Vector3fConst.backwards * Time.deltaTime * speed
            if (transform.position.z() > 50f) {
                transform.position += Vector3fConst.forward * 200f
            }
        } else {
            transform.position += Vector3fConst.forward * Time.deltaTime * speed
            if (transform.position.z() < -150f) {
                transform.position += Vector3fConst.backwards * 200f
            }
        }
    }

    override fun draw() {
    }

}
