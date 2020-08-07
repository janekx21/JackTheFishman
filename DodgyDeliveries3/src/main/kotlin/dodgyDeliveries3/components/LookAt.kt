package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.minus
import jackTheFishman.engine.math.moveTowards
import org.joml.Quaternionf
import org.joml.Vector3fc

class LookAt(var target: Transform? = null, var speed: Float = 1f) : Component() {
    var smoothTargetPosition: Vector3fc = Vector3fConst.zero

    override fun update() {
        if (target != null) {
            val delta = target!!.position - smoothTargetPosition
            smoothTargetPosition = smoothTargetPosition.moveTowards(target!!.position, Time.deltaTime * speed * delta.length())

            val direction = smoothTargetPosition - transform.position
            val targetRotation = Quaternionf().lookAlong(direction, Vector3fConst.up)
            targetRotation.invert()
            transform.rotation = targetRotation
        }
    }

    override fun draw() {}
}
