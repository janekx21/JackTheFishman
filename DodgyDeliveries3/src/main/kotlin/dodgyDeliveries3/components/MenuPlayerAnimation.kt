package dodgyDeliveries3.components

import dodgyDeliveries3.Component
import jackTheFishman.engine.Time
import jackTheFishman.engine.math.Vector3fConst
import org.joml.Quaternionf
import kotlin.math.sin

class MenuPlayerAnimation : Component() {
    override fun update() {
        transform.rotation = Quaternionf()
            .rotateY(Math.toRadians(-70.0).toFloat())
            .rotateAxis(sin(Time.time * 3f) * .1f, Vector3fConst.right)

    }
}
