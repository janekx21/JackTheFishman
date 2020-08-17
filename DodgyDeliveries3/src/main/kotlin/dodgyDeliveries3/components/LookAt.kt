package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import jackTheFishman.engine.math.minus
import jackTheFishman.engine.math.moveTowards
import jackTheFishman.engine.math.plus
import org.joml.Quaternionf
import org.joml.Vector3fc

class LookAt(var target: Transform? = null, var speed: Float = 1f, var offset: Vector3fc = Vector3fConst.zero) :
    Component() {
    var smoothTargetPosition: Vector3fc = Vector3fConst.zero

    override fun update() {
        if (target != null) {
            val targetPosition = target!!.position + offset
            val delta = targetPosition - smoothTargetPosition
            smoothTargetPosition =
                smoothTargetPosition.moveTowards(targetPosition, Time.deltaTime * speed * delta.length())

            val direction = smoothTargetPosition - transform.position
            val targetRotation = Quaternionf().lookAlong(direction, Vector3fConst.up)
            targetRotation.invert()
            transform.rotation = targetRotation
        }
    }

    override fun draw() {}
}
